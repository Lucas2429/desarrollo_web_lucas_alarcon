from sqlalchemy import create_engine, Column, Integer, BigInteger, String, ForeignKey, DateTime, Enum
from sqlalchemy.orm import sessionmaker, declarative_base, relationship


DB_NAME = "tarea2"
DB_USERNAME = "cc5002" 
DB_PASSWORD = "programacionweb" 
DB_HOST = "localhost"
DB_PORT = 3306
DB_CHARSET = "utf8"

DATABASE_URL = f"mysql+pymysql://{DB_USERNAME}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"

engine = create_engine(DATABASE_URL, echo=False, future=True)
SessionLocal = sessionmaker(bind=engine)

Base = declarative_base()

# --- Models ---

class Region(Base):
    __tablename__ = 'region'

    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(200), nullable=False)

    comunas = relationship("Comuna", back_populates="region")


class Comuna(Base):
    __tablename__ = 'comuna'

    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(200), nullable=False)
    region_id = Column(Integer, ForeignKey('region.id'), nullable=False)

    region = relationship("Region", back_populates="comunas")
    actividades = relationship("Actividad", back_populates="comuna")


class Actividad(Base):
    __tablename__ = 'actividad'

    id = Column(Integer, primary_key=True, autoincrement=True)
    comuna_id = Column(Integer, ForeignKey('comuna.id'), nullable=False)
    sector = Column(String(100), nullable=True)
    nombre = Column(String(200), nullable=False)
    email = Column(String(100), nullable=False)
    celular = Column(String(15), nullable=True)
    dia_hora_inicio = Column(DateTime, nullable=False)
    dia_hora_termino = Column(DateTime, nullable=True)
    descripcion = Column(String(500), nullable=True)

    comuna = relationship("Comuna", back_populates="actividades")
    fotos = relationship("Foto", back_populates="actividad", cascade="all, delete")
    contactos = relationship("ContactarPor", back_populates="actividad", cascade="all, delete")
    temas = relationship("ActividadTema", back_populates="actividad", cascade="all, delete")


class Foto(Base):
    __tablename__ = 'foto'

    id = Column(Integer, primary_key=True, autoincrement=True)
    actividad_id = Column(Integer, ForeignKey('actividad.id'), primary_key=True)
    ruta_archivo = Column(String(300), nullable=False)
    nombre_archivo = Column(String(300), nullable=False)

    actividad = relationship("Actividad", back_populates="fotos")


class ContactarPor(Base):
    __tablename__ = 'contactar_por'

    id = Column(Integer, primary_key=True, autoincrement=True)
    actividad_id = Column(Integer, ForeignKey('actividad.id'), primary_key=True)
    nombre = Column(Enum('whatsapp', 'telegram', 'X', 'instagram', 'tiktok', 'otra'), nullable=False)
    identificador = Column(String(150), nullable=False)

    actividad = relationship("Actividad", back_populates="contactos")


class ActividadTema(Base):
    __tablename__ = 'actividad_tema'

    id = Column(Integer, primary_key=True, autoincrement=True)
    actividad_id = Column(Integer, ForeignKey('actividad.id'), primary_key=True)
    tema = Column(Enum('música', 'deporte', 'ciencias', 'religión', 'política', 'tecnología', 'juegos', 'baile', 'comida', 'otro'), nullable=False)
    glosa_otro = Column(String(15), nullable=True)

    actividad = relationship("Actividad", back_populates="temas")

# --- Database Functions ---
def get_actividades(page_size):
    session = SessionLocal()
    actividades = session.query(Actividad).limit(page_size).all()
    session.close()
    return actividades

def get_all_actividades():
    session = SessionLocal()
    actividades = session.query(Actividad).all()
    session.close()
    return actividades

def get_comuna_id_by_name(name):
    session = SessionLocal()
    comuna = session.query(Comuna).filter_by(nombre=name).first()
    comuna_id = comuna.id if comuna else None
    session.close()
    return comuna_id

def get_comuna_by_id(comuna_id):
    session = SessionLocal()
    comuna = session.query(Comuna).filter_by(id=comuna_id).first()
    session.close()
    return comuna

def get_tema_by_id(actividad_id):
    session = SessionLocal()
    actividadTema = session.query(ActividadTema).filter_by(actividad_id=actividad_id).first()
    tema = actividadTema.tema if actividadTema.tema!= 'otro' else actividadTema.glosa_otro
    session.close()
    return tema

def get_last_actividad_id():
    session = SessionLocal()
    last_actividad = session.query(Actividad).order_by(Actividad.id.desc()).first()
    last_id = last_actividad.id if last_actividad else None
    session.close()
    return last_id

def create_actividad(comuna_id, sector, nombre, email, 
					celular, dia_hora_inicio, dia_hora_termino, descripcion):
    session = SessionLocal()
    nueva_actividad = Actividad(comuna_id=comuna_id, sector=sector, nombre=nombre,
								email=email, celular=celular, 
								dia_hora_inicio=dia_hora_inicio, 
								dia_hora_termino=dia_hora_termino, 
								descripcion=descripcion)
    session.add(nueva_actividad)
    session.commit()
    session.close()

def create_actividad_tema(actividad_id, tema, glosa_otro=None):
    session = SessionLocal()
    nuevo_tema = ActividadTema(actividad_id=actividad_id, tema=tema, glosa_otro=glosa_otro)
    session.add(nuevo_tema)
    session.commit()
    session.close()

def create_contacto_por(actividad_id, nombre, identificador):
    session = SessionLocal()
    nuevo_contacto = ContactarPor(actividad_id=actividad_id, nombre=nombre, identificador=identificador)
    session.add(nuevo_contacto)
    session.commit()
    session.close()

def create_foto(actividad_id, ruta_archivo, nombre_archivo):
    session = SessionLocal()
    nueva_foto = Foto(actividad_id=actividad_id, ruta_archivo=ruta_archivo, nombre_archivo=nombre_archivo)
    session.add(nueva_foto)
    session.commit()
    session.close()

