from flask import Flask, render_template, request, redirect, url_for
from markupsafe import escape
import re
import pymysql
from database import db

app = Flask(__name__)

@app.route('/')
@app.route('/home')
def index():
    return render_template('home.html')

@app.route('/actividades/')
def actividades():
    return render_template('actividades.html')

@app.route('/estadisticas/')
def estadisticas():
    return render_template('estadisticas.html')

@app.get('/Agregar Actividad/')
def agregar_actividad():
    return render_template('agregar_actividad.html')

