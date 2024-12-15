# README

## Descripción
Esta aplicación es un sistema de carrito de compras con funcionalidades de navegación entre fragmentos, manejo de productos mediante `RecyclerView`, y comunicación con una API externa. Es de vital importancia ejecutar previamente el proyecto con el [BackEnd](https://github.com/RedRiotTank/DSSP1).

## Instrucciones de Instalación y Ejecución

1. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/MiixZ/Catalogo
   cd Catalogo
   ```

2. **Abrir el Proyecto**
   - Utilizar **Android Studio** (versión recomendada: 2022.2.1 o superior).

3. **Configurar las Dependencias**
   - Habrá que asegurarse de tener configurado el archivo `build.gradle` con las dependencias necesarias (mencionadas abajo).

4. **Ejecutar la Aplicación**
   - Conecta un dispositivo físico o utiliza un emulador.
   - Presiona el botón de **Run** (Shift + F10 en Windows/Linux o Control + R en macOS).

## Dependencias Usadas

Estas dependencias están definidas en el archivo `build.gradle` (módulo app):

- **Retrofit**: Cliente HTTP para Android. Facilita la comunicación con servicios RESTful al simplificar las peticiones HTTP y el manejo de respuestas.
   ```gradle
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
   ```
   + **Retrofit Converter-Gson**: Integra Retrofit con Gson para convertir automáticamente las respuestas JSON en objetos Java.

- **Gson**: Librería para parseo y serialización de JSON. Facilita la conversión entre objetos Java y JSON.
   ```gradle
   implementation 'com.google.code.gson:gson:2.8.9'
   ```

- **Google Maps API**: Proporciona las herramientas necesarias para integrar mapas de Google en la aplicación. Permite mostrar mapas interactivos, ubicaciones y rutas en tiempo real.
   ```gradle
   implementation 'com.google.android.gms:play-services-maps:18.1.0'
   implementation 'com.google.android.gms:play-services-location:21.3.0'
   ```

- **AndroidX Core**: Proporciona funciones esenciales para desarrollos en Android con soporte de Kotlin.
   ```gradle
   implementation(libs.androidx.core.ktx)
   ```

- **AndroidX AppCompat**: Ofrece soporte para compatibilidad de versiones anteriores de Android.
   ```gradle
   implementation(libs.androidx.appcompat)
   ```

- **Material Components**: Permite el uso de componentes modernos de UI basados en Material Design.
   ```gradle
   implementation(libs.material)
   ```

- **ConstraintLayout**: Layout eficiente y flexible para interfaces de usuario complejas.
   ```gradle
   implementation(libs.androidx.constraintlayout)
   ```

- **AndroidX Lifecycle**:
   - ViewModel y LiveData: Manejo del ciclo de vida y datos observables en la UI.
   ```gradle
   implementation(libs.androidx.lifecycle.livedata.ktx)
   implementation(libs.androidx.lifecycle.viewmodel.ktx)
   ```

- **Navigation Component**: Manejo de navegación entre fragmentos.
   ```gradle
   implementation(libs.androidx.navigation.fragment.ktx)
   implementation(libs.androidx.navigation.ui.ktx)
   ```

## Estructura de Carpetas

La estructura principal de la carpeta `app` es la siguiente:

```
app/
├── java/
│   └── com.example.<nombre_del_paquete>/
│       ├── activities/        # Contiene las Activities principales
│       ├── fragments/         # Fragmentos para navegación
│       ├── adapters/          # Adaptadores para RecyclerView
│       ├── models/            # Clases modelo para datos
│       ├── repositories/      # Repositorios para manejar la lógica de la API
│       └── utils/             # Utilidades y funciones auxiliares
├── res/
│   ├── layout/            # Archivos XML de diseño
│   ├── values/            # Strings, estilos y otros recursos
│   └── drawable/         # Imágenes y recursos visuales
└── AndroidManifest.xml      # Archivo de configuración de la aplicación
```

## Endpoints de la API

La aplicación utiliza los siguientes endpoints a través de **Retrofit**:

| Método | Endpoint                 | Descripción                                          |
|---------|--------------------------|------------------------------------------------------|
| GET     | `/api/products`          | Obtiene la lista de productos.                      |
| POST    | `/api/cart/add`          | Añade un producto al carrito.                       |
| DELETE  | `/api/cart/remove/{id}`  | Elimina un producto del carrito.                     |
| POST    | `/api/checkout`          | Realiza el checkout y procesa el pedido.             |
