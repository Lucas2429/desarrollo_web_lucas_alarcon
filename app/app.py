from flask import Flask, render_template, request, redirect, url_for
from markupsafe import escape
import re
import pymysql
from database import db

app = Flask(__name__)

@app.route('/')
@app.route('/home')
def index():
    data = []
    for actividad in db.get_actividades(page_size=5):
        comuna = db.get_comuna(actividad.comuna_id)
        tema = db.get_tema(actividad.id)
        data.append({
            "Inicio": actividad.dia_hora_inicio,
            "Termino": actividad.dia_hora_termino,
            "Comuna": comuna.nombre,
            "Sector": actividad.sector,
            "Tema": tema.tema,
            "Foto": "PlaceHolder",

        })
    
    return render_template('home.html', actividades=actividades)

@app.route('/actividades')
def actividades():
    data = []
    for actividad in db.get_actividades(20):
        comuna = db.get_comuna(actividad.comuna_id)
        tema = db.get_tema(actividad.id)
        data.append({
            "Inicio": actividad.dia_hora_inicio,
            "Termino": actividad.dia_hora_termino,
            "Comuna": comuna.nombre,
            "Sector": actividad.sector,
            "Tema": tema.tema,
            "Organizador": actividad.nombre,
            "Fotos": [],

        })
    return render_template('actividades.html', data=data)

@app.route('/estadisticas')
def estadisticas():
    return render_template('estadisticas.html')

@app.route('/agregar_actividad', methods=['GET', 'POST'])
def agregar_actividad():
    if request.method == 'POST':
        region = request.form.get('region')
        comuna = request.form.get('comuna')
        sector = request.form.get('sector')
        nombre = request.form.get('nombre')
        email = request.form.get('email')
        telefono = request.form.get('telefono')
        contacto = request.form.get('contacto')
        otroContacto = request.form.get('idContacto')
        inicio = request.form.get('inicio')
        termino = request.form.get('termino')
        descripcion = request.form.get('descripcion')
        tema = request.form.get('tema')
        otroTema = request.form.get('otroTema')
        foto = request.form.get('foto')

        # Carga a la base de datos
        db.create_actividad(
            comuna_id=comuna,
            sector=sector,
            nombre=nombre,
            email=email,
            celular=telefono,
            dia_hora_inicio=inicio, 
            dia_hora_termino=termino, 
            descripcion=descripcion
        )

        return redirect(url_for('actividades'))

    elif request.method == 'GET':
        return render_template('agregar_actividad.html')

