from flask import Flask, render_template, request, redirect, url_for, jsonify
from markupsafe import escape
from flask_cors import cross_origin
import re
import pymysql
from database import db
from datetime import datetime, timedelta
import time
import random


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

@app.route('/actividades', methods=['GET'])
def actividades():
    if request.method == 'GET':
        page = request.args.get('page', 1, type=int)
        per_page = 5
        actividades = db.get_all_actividades()
        start = (page - 1) * per_page
        end = start + per_page
        total_actividades = len(actividades)
        total_pages = (total_actividades + per_page - 1) // per_page

        actividades_on_page = []
        for actividad in actividades[start:end]:
            comentarios = db.get_comentarios_by_actividad_id(actividad.id)
            comentarios_en_actividad = []
            for comentario in comentarios:
                comentarios_en_actividad.append({
                    "usuario": comentario.nombre,
                    "comentario": comentario.texto,
                    "fecha": comentario.fecha
                })
            comuna = db.get_comuna_by_id(actividad.comuna_id)
            tema = db.get_tema_by_id(actividad.id)
            actividades_on_page.append({
                "id": actividad.id,
                "Inicio": actividad.dia_hora_inicio,
                "Termino": actividad.dia_hora_termino,
                "Comuna": comuna.nombre,
                "Sector": actividad.sector,
                "Tema": tema,
                "Organizador": actividad.nombre,
                "Foto": "PlaceHolder",
                "comentarios": comentarios_en_actividad
            })
        
        return render_template('actividades.html', actividades_on_page=actividades_on_page,
                            page=page, total_pages=total_pages, total_actividades=total_actividades)

@app.route('/post_comentario', methods=['POST'])
def post_comentario():
    usuario = request.form.get("usuario")
    comentario = request.form.get("comentario")
    fecha = datetime.now()
    actividad_id = request.form.get("actividad_id")

    # Validar que no falten campos
    if not usuario or not comentario or not actividad_id:
        return jsonify({"error": "Todos los campos son obligatorios"}), 400

    # Validar longitud mínima del comentario
    if len(comentario.strip()) < 5:
        return jsonify({"error": "El comentario debe tener al menos 5 caracteres"}), 400

    # Insertar comentario en la base de datos (asumiendo que tienes una función para esto)
    db.create_comentario(
        nombre=usuario.strip(),
        texto=comentario.strip(),
        fecha=fecha,
        actividad_id=int(actividad_id)
    )

    # Respuesta JSON para que el frontend la muestre sin recargar
    return jsonify({"message": "Comentario agregado exitosamente"}), 200



@app.route('/estadisticas')
def estadisticas():
    return render_template('estadisticas.html')

@app.route('/api/actividades-por-dia', methods=['GET'])
@cross_origin(origin="127.0.0.1", supports_credentials=True)
def actividades_por_dia():
    # Datos aleatorios para simular actividades por día
    dias = [datetime(2025, 7, d).strftime("%Y-%m-%d") for d in range(1, 11)]  # 10 días
    cantidades = [random.randint(1, 10) for _ in dias]
    time.sleep(1)
    return jsonify({"dias": dias, "cantidades": cantidades})

@app.route('/api/actividades-por-tipo', methods=['GET'])
@cross_origin(origin="127.0.0.1", supports_credentials=True)
def actividades_por_tipo():
    # Datos aleatorios para simular actividades por tipo
    tipos = ['música', 'deporte', 'ciencias', 'religión', 'política', 'tecnología', 'juegos', 'baile', 'comida', 'otro']
    data = [{"name": tipo, "y": random.randint(5, 20)} for tipo in tipos]
    time.sleep(1)
    return jsonify({"tipos": data})

@app.route('/api/actividades-por-mes', methods=['GET'])
@cross_origin(origin="127.0.0.1", supports_credentials=True)
def actividades_por_mes_franja():
    # Datos aleatorios para simular actividades por mes y franja horaria
    meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo"]
    manana = [random.randint(1, 10) for _ in meses]
    mediodia = [random.randint(1, 10) for _ in meses]
    tarde = [random.randint(1, 10) for _ in meses]
    time.sleep(1)
    return jsonify({
        "meses": meses,
        "manana": manana,
        "mediodia": mediodia,
        "tarde": tarde
    })


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
    

