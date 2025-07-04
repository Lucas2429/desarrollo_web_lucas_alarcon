from flask import Flask, render_template, request, redirect, url_for
from markupsafe import escape
import re
import pymysql
from database import db

app = Flask(__name__)

@app.route('/')
@app.route('/home')
def index():
    actividades = []
    for actividad in db.get_actividades(page_size=5):
        comuna = db.get_comuna_by_id(actividad.comuna_id)
        tema = db.get_tema_by_id(actividad.id)
        actividades.append({
            "Inicio": actividad.dia_hora_inicio,
            "Termino": actividad.dia_hora_termino,
            "Comuna": comuna.nombre,
            "Sector": actividad.sector,
            "Tema": tema,
            "Foto": "PlaceHolder",
        })
    
    return render_template('home.html', actividades=actividades)

@app.route('/actividades')
def actividades():
    page = request.args.get('page', 1, type=int)
    per_page = 5
    actividades = db.get_all_actividades()
    start = (page - 1) * per_page
    end = start + per_page
    total_actividades = len(actividades)
    total_pages = (total_actividades + per_page - 1) // per_page

    actividades_on_page = []
    for actividad in actividades[start:end]:
        comuna = db.get_comuna_by_id(actividad.comuna_id)
        tema = db.get_tema_by_id(actividad.id)
        actividades_on_page.append({
            "Inicio": actividad.dia_hora_inicio,
            "Termino": actividad.dia_hora_termino,
            "Comuna": comuna.nombre,
            "Sector": actividad.sector,
            "Tema": tema,
            "Organizador": actividad.nombre,
            "Foto": "PlaceHolder",
        })
    
    return render_template('actividades.html', actividades_on_page=actividades_on_page,
                           page=page, total_pages=total_pages, total_actividades=total_actividades)

@app.route('/estadisticas')
def estadisticas():
    return render_template('estadisticas.html')

@app.route('/agregar_actividad', methods=['GET'])
def agregar_actividad():
    if request.method == 'GET':
        return render_template('agregar_actividad.html')

@app.route('/post_actividad', methods=['POST'])
def post_actividad():
    if request.method == 'POST':
        region = request.form.get('region')
        comuna = request.form.get('comuna')
        sector = request.form.get('sector')
        nombre = request.form.get('nombre')
        email = request.form.get('email')
        telefono = request.form.get('telefono')
        contacto = request.form.get('contacto')
        idContacto = request.form.get('idContacto')
        inicio = request.form.get('inicio')
        termino = request.form.get('termino')
        descripcion = request.form.get('descripcion')
        tema = request.form.get('tema')
        otroTema = request.form.get('otroTema')
        foto = request.form.get('foto')
        foto2 = request.form.get('foto2')
        foto3 = request.form.get('foto3')
        foto4 = request.form.get('foto4')
        foto5 = request.form.get('foto5')

        fotos = [foto, foto2, foto3, foto4, foto5]

        # Carga a la base de datos
        db.create_actividad(
            comuna_id=db.get_comuna_id_by_name(comuna),
            sector=sector,
            nombre=nombre,
            email=email,
            celular=telefono,
            dia_hora_inicio=inicio, 
            dia_hora_termino=termino, 
            descripcion=descripcion
        )

        db.create_actividad_tema(
            actividad_id=db.get_last_actividad_id(),
            tema=tema,
            glosa_otro=otroTema if otroTema else None
        )

        db.create_contacto_por(
            actividad_id=db.get_last_actividad_id(),
            nombre=contacto,
            identificador=idContacto
        )

        #for foto in fotos:
        #    if foto:
        #        db.create_foto(
        #            actividad_id=db.get_last_actividad_id(),
        #            ruta_archivo=foto,
        #            nombre_archivo=foto.split('/')[-1] 
        #        )

        return redirect(url_for('actividades'))
