package com.tarea4.tarea4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tarea4.tarea4.models.*;
import com.tarea4.tarea4.repositories.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private ActividadTemaRepository actividadTemaRepository;

    @Autowired
    private ContactarPorRepository contactarPorRepository;

    @Autowired
    private FotoRepository fotoRepository;

    private final String pathStatic;
    public AppService() throws IOException {
        // Dynamically resolve the absolute path for the static directory
        Path staticDir = Paths.get(ResourceUtils.getFile("classpath:static").getAbsolutePath());
        this.pathStatic = staticDir.toString();
        System.out.println("Static path resolved to: " + this.pathStatic);
    }

    // === FUNCIONES ===

    public List<Actividad> getUltimasActividades(int cantidad) {
        return actividadRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Actividad::getDiaHoraInicio).reversed())
                .limit(cantidad)
                .toList();
    }

    public List<Actividad> getTodasLasActividades() {
        return actividadRepository.findAll();
    }

    public Optional<Comuna> getComunaById(int id) {
        return comunaRepository.findById(id);
    }

    public Optional<Comuna> getComunaByNombre(String nombre) {
        return comunaRepository.findByNombre(nombre);
    }

    public Optional<ActividadTema> getTemaByActividadId(int actividadId) {
        return actividadTemaRepository.findFirstByActividadId(actividadId);
    }

    public Optional<Actividad> getUltimaActividad() {
        return actividadRepository.findAll()
                .stream()
                .max(Comparator.comparing(Actividad::getId));
    }

    public void crearActividad(String comunaNombre, String sector, String nombre, String email,
            String celular, String contacto, String idContacto, LocalDateTime inicio, LocalDateTime termino, String descripcion,
            String tema, String otroTema, MultipartFile[] fotos) throws NoSuchAlgorithmException, IOException {
        
        if (fotos.length <= 5) {
            Comuna comuna = comunaRepository.findByNombre(comunaNombre)
                .orElseThrow(() -> new IllegalArgumentException("Comuna no encontrada: " + comunaNombre));

            // Save the confession in the database
            Actividad actividad = new Actividad(
                comuna, 
                sector,
                nombre,
                email,
                celular,
                inicio,
                termino,
                descripcion
            );

            actividadRepository.save(actividad);
            System.out.println("Actividad guardada.");

            for(MultipartFile foto : fotos) {
                String _originalFilename = foto.getOriginalFilename();
                if (_originalFilename == null || _originalFilename.isEmpty()) {
                    throw new IllegalArgumentException("File name is empty.");
                }
                // Generate unique filename
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(_originalFilename.getBytes("UTF-8"));
                byte[] hash = md.digest();
                String _filename;
                try (Formatter formatter = new Formatter()) {
                    for (byte b : hash) {
                        formatter.format("%02x", b);
                    }
                    _filename = formatter.toString();
                }

                String _extension = _originalFilename.substring(_originalFilename.lastIndexOf('.') + 1).toLowerCase();
                if (!_extension.matches("jpg|jpeg|png|gif")) {
                    throw new IllegalArgumentException("Invalid file extension: " + _extension);
                }

                String imgFilename = _filename + "." + _extension;
                String relativePathImg = "/uploads/" + imgFilename;
                String finalPath = pathStatic + relativePathImg;

                System.out.println("Final image path: " + finalPath);

                // Ensure the uploads directory exists
                Path directoryPath = Paths.get(pathStatic + "/uploads");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                    System.out.println("Uploads directory created.");
                }

                // Save the image file
                Path path = Paths.get(finalPath);
                try (InputStream inputStream = foto.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File successfully saved at: " + path.toAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save the image file.", e);
                }

                // create image
                Foto nuevaFoto = new Foto();
                nuevaFoto.setRutaArchivo(relativePathImg);
                nuevaFoto.setActividad(actividad);
                nuevaFoto.setNombreArchivo(imgFilename);
                fotoRepository.save(nuevaFoto);
            }
        }
    }

    public void crearActividadTema(ActividadTema actividadTema) {
        actividadTemaRepository.save(actividadTema);
    }

    public void crearContactoPor(ContactarPor contacto) {
        contactarPorRepository.save(contacto);
    }

    public void crearFoto(Foto foto) {
        fotoRepository.save(foto);
    }

    public Map<String, Object> getActividadesPaginadas(int page) {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        List<Actividad> todas = actividadRepository.findAll();
        List<Map<String, Object>> actividadesPaginadas = new ArrayList<>();

        todas.stream()
            .skip(offset)
            .limit(pageSize)
            .forEach(actividad -> {
                Comuna comuna = actividad.getComuna();
                Optional<ActividadTema> temaOpt = actividadTemaRepository.findFirstByActividadId(actividad.getId());

                Map<String, Object> actividadMap = new HashMap<>();
                actividadMap.put("id", actividad.getId());
                actividadMap.put("inicio", actividad.getDiaHoraInicio());
                actividadMap.put("termino", actividad.getDiaHoraTermino());
                actividadMap.put("comuna", comuna != null ? comuna.getNombre() : "Desconocida");
                actividadMap.put("sector", actividad.getSector());
                actividadMap.put("tema", temaOpt.map(t -> {
                    if ("otro".equals(t.getTema())) {
                        return t.getGlosaOtro() != null ? t.getGlosaOtro() : "Otro";
                    } else {
                        return t.getTema();
                    }
                }).orElse("Desconocido"));
                actividadMap.put("organizador", actividad.getNombre());
                actividadMap.put("foto", "PlaceHolder");

                actividadesPaginadas.add(actividadMap);
            });


        Map<String, Object> resultado = new HashMap<>();
        resultado.put("actividades", actividadesPaginadas);
        resultado.put("totalActividades", todas.size());
        resultado.put("page", page);
        resultado.put("totalPages", (int) Math.ceil((double) todas.size() / 5));
        return resultado;
    }

}

