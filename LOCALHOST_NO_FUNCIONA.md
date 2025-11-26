# 🚨 LOCALHOST NO FUNCIONA - SOLUCIÓN RÁPIDA

## ❌ PROBLEMA
```
localhost:8080 no responde
```

## ✅ CAUSA
```
La aplicación NO está corriendo
```

## 🔧 SOLUCIÓN EN 3 PASOS

### 1️⃣ ABRIR EN INTELLIJ IDEA
```
IntelliJ IDEA → File → Open
Seleccionar: Servicio_Calendario
```

### 2️⃣ EJECUTAR LA APLICACIÓN
```
Buscar archivo: CalendarioApplication.java
Click derecho → Run 'CalendarioApplication'
```

### 3️⃣ VERIFICAR QUE FUNCIONA
```
Abrir navegador:
http://localhost:8080/swagger-ui.html
```

---

## 📺 GUÍA VISUAL

```
┌──────────────────────────────────────┐
│  IntelliJ IDEA                       │
├──────────────────────────────────────┤
│                                      │
│  Project                             │
│  └─ src                              │
│     └─ main                          │
│        └─ java                       │
│           └─ org.example             │
│              └─ CalendarioApplication│ ← Click derecho aquí
│                                      │
│  [Run 'CalendarioApplication']       │ ← Seleccionar esto
│                                      │
└──────────────────────────────────────┘

         ↓ EJECUTA ↓

┌──────────────────────────────────────┐
│  Console                             │
├──────────────────────────────────────┤
│                                      │
│  Started CalendarioApplication       │ ← Busca este mensaje
│  in 3.456 seconds                    │
│                                      │
└──────────────────────────────────────┘

         ↓ AHORA SÍ ↓

┌──────────────────────────────────────┐
│  Navegador                           │
├──────────────────────────────────────┤
│                                      │
│  http://localhost:8080/swagger-ui    │
│                                      │
│  ✅ Swagger UI funcionando           │
│                                      │
└──────────────────────────────────────┘
```

---

## ⚡ ATAJOS DE TECLADO

```
⌘ + ; (Mac)     = Project Structure
⌘ + F9 (Mac)    = Build Project
⌘ + R (Mac)     = Run
```

---

## 🎯 VERIFICACIÓN

### ✅ Aplicación corriendo:
```
Console muestra: "Started CalendarioApplication"
```

### ✅ LocalHost funcionando:
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/h2-console
```

---

## 🆘 SI NO FUNCIONA

1. **File → Invalidate Caches → Restart**
2. **Maven panel → Reload All Maven Projects**
3. **Build → Rebuild Project**
4. **Reintentar Run**

---

## 📝 COMANDOS TERMINAL

```bash
# Verificar si está corriendo
lsof -i:8080

# Si sale algo = ✅ está corriendo
# Si no sale nada = ❌ no está corriendo
```

---

**RECUERDA**: 
- ✅ Usar **IntelliJ IDEA** para ejecutar
- ❌ NO usar `mvn spring-boot:run` (tiene problemas)

---

**Lee el archivo completo**: `SOLUCION_LOCALHOST.md`

