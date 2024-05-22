db.createCollection("estaciones", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nombre", "geolocalizacion", "direccion", "volumen_medio"],
      properties: {
        nombre: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        geolocalizacion: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        direccion: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        volumen_medio: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
      },
    },
  },
});

    db.createCollection("guaguas", {
    validator: {
        $jsonSchema: {
        bsonType: "object",
        required: [
            "matricula",
            "capacidad",
            "motor",
            "consumo_medio",
            "f_mantenimiento",
            "kilometros",
            "num_linea",
        ],
        properties: {
            matricula: {
            bsonType: "string",
            description: "debe ser un string y es obligatorio",
            },
            capacidad: {
            bsonType: "int",
            description: "debe ser un int y es obligatorio",
            },
            motor: {
            bsonType: "string",
            description: "debe ser un string y es obligatorio",
            },
            consumo_medio: {
            bsonType: "decimal",
            description: "debe ser un decimal y es obligatorio",
            },
            f_mantenimiento: {
            bsonType: "date",
            description: "debe ser un date y es obligatorio",
            },
            kilometros: {
            bsonType: "int",
            description: "debe ser un int y es obligatorio",
            },
            num_linea: {
            bsonType: "int",
            description: "debe ser un entero y es obligatorio",
            
            },
        },
        },
    },
    });

db.createCollection("jornadas", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["fecha", "turno"],
      properties: {
        fecha: {
          bsonType: "date",
          description: "debe ser un date y es obligatorio",
        },
        turno: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
      },
    },
  },
});

db.createCollection("chofers", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["dni", "nombre", "direccion", "telefono", "dni_supervisor"],
      properties: {
        dni: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        nombre: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        direccion: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        telefono: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        dni_supervisor: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
      },
    },
  },
});

db.createCollection("pertenecen_estacion", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["id_estacion", "num_linea", "tiempo_medio", "orden"],
      properties: {
        id_estacion: {
          bsonType: "objectId",
          description: "debe ser un objectId y es obligatorio",
        },
        num_linea: {
          bsonType: "objectId",
          description: "debe ser un objectId y es obligatorio",
        },
        tiempo_medio: {
          bsonType: "decimal",
          description: "debe ser un decimal y es obligatorio",
        },
        orden: {
          bsonType: "int",
          description: "debe ser un int y es obligatorio",
        },
      },
    },
  },
});

db.createCollection("trabajan", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["dni_chofer", "id_jornada", "matricula", "consumo_chofer"],
      properties: {
        dni_chofer: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        id_jornada: {
          bsonType: "objectId",
          description: "debe ser un objectId y es obligatorio",
        },
        matricula: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        consumo_chofer: {
          bsonType: "decimal",
          description: "debe ser un decimal y es obligatorio",
        },
      },
    },
  },
});

db.createCollection("conducen", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["dni_chofer", "matricula", "consumo_chofer"],
      properties: {
        dni_chofer: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        matricula: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
        consumo_chofer: {
          bsonType: "decimal",
          description: "debe ser un decimal y es obligatorio",
        },
      },
    },
  },
});

db.createCollection("asignan", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["num_linea", "matricula"],
      properties: {
        num_linea: {
          bsonType: "objectId",
          description: "debe ser un objectId y es obligatorio",
        },
        matricula: {
          bsonType: "string",
          description: "debe ser un string y es obligatorio",
        },
      },
    },
  },
});
