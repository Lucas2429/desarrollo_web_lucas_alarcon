package com.tarea4.tarea4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tarea4.tarea4.models.*;
import com.tarea4.tarea4.repositories.ActividadRepository;
import com.tarea4.tarea4.repositories.ActividadTemaRepository;
import com.tarea4.tarea4.repositories.ComentarioRepository;
import com.tarea4.tarea4.repositories.ComunaRepository;
import com.tarea4.tarea4.repositories.ContactarPorRepository;
import com.tarea4.tarea4.repositories.FotoRepository;
import com.tarea4.tarea4.repositories.NotaRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ActividadTemaRepository actividadTemaRepository;

    @Autowired
    private ContactarPorRepository contactarPorRepository;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private NotaRepository notaRepository;

    // === FUNCIONES ===

    public List<Map<String, Object>> getActividadesFinalizadas() {
        List<Actividad> todas = actividadRepository.findAll();
        List<Map<String, Object>> finalizadas = new ArrayList<>();

        for (Actividad actividad : todas) {
            if (actividad.getDiaHoraTermino() != null &&
                actividad.getDiaHoraTermino().isBefore(LocalDateTime.now())) {

                Optional<ActividadTema> temaOpt = actividadTemaRepository.findFirstByActividadId(actividad.getId());

                double promedio = notaRepository.findByActividadId(actividad.getId())
                    .stream()
                    .mapToInt(Nota::getNota)
                    .average()
                    .orElse(Double.NaN);

                Map<String, Object> map = new HashMap<>();
                map.put("id", actividad.getId());
                map.put("inicio", actividad.getDiaHoraInicio().toLocalDate());
                map.put("sector", actividad.getSector());
                map.put("nombre", actividad.getNombre());
                map.put("tema", temaOpt.map(t -> t.getTema().toString()).orElse("otro"));
                map.put("nota", Double.isNaN(promedio) ? "-" : Math.round(promedio * 10.0) / 10.0);
                finalizadas.add(map);
            }
        }

        return finalizadas;
    }


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

    public List<Comentario> getComentariosPorActividad(int actividadId) {
        return comentarioRepository.findByActividadId(actividadId);
    }

    public void crearActividad(Actividad actividad) {
        actividadRepository.save(actividad);
    }

    public void crearActividadTema(ActividadTema actividadTema) {
        actividadTemaRepository.save(actividadTema);
    }

    public void crearContactoPor(ContactarPor contacto) {
        contactarPorRepository.save(contacto);
    }

    public void crearComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
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

                List<Map<String, Object>> comentariosLista = comentarioRepository.findByActividadId(actividad.getId())
                    .stream().map(comentario -> {
                        Map<String, Object> c = new HashMap<>();
                        c.put("usuario", comentario.getNombre());
                        c.put("comentario", comentario.getTexto());
                        c.put("fecha", comentario.getFecha());
                        return c;
                    }).toList();

                actividadMap.put("comentarios", comentariosLista);

                actividadesPaginadas.add(actividadMap);
            });


        Map<String, Object> resultado = new HashMap<>();
        resultado.put("actividades", actividadesPaginadas);
        resultado.put("totalActividades", todas.size());
        resultado.put("page", page);
        resultado.put("totalPages", (int) Math.ceil((double) todas.size() / 5));
        return resultado;
    }


    public void crearComentario(String usuario, String comentario, LocalDateTime now, int actividad_id) {
        throw new UnsupportedOperationException("Unimplemented method 'crearComentario'");
    }

    public void crearActividad(String region, String comuna, String sector, String nombre, String email,
            String telefono, String contacto, String idContacto, String inicio, String termino, String descripcion,
            String tema, String otroTema, MultipartFile[] fotos) {
        throw new UnsupportedOperationException("Unimplemented method 'crearActividad'");
    }
    // public double calcularPromedioNotas(int actividadId) {
    //    List<Nota> notas = notaRepository.findByActividadId(actividadId);
    //    return notas.isEmpty() ? -1 : notas.stream().mapToInt(Nota::getNota).average().orElse(-1);
    // }
    

}

