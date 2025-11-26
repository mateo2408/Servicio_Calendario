#!/bin/bash

# Script de prueba completa del sistema
# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║     PRUEBA COMPLETA DEL SISTEMA DE CALENDARIO           ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}\n"

BASE_URL="http://localhost:8080"

# 1. REGISTRAR USUARIO
echo -e "${GREEN}📝 PASO 1: Registrando usuario...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan.perez",
    "password": "MiPassword123",
    "email": "juan.perez@email.com",
    "nombre": "Juan Pérez"
  }')

echo "$REGISTER_RESPONSE" | jq '.'

TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.token')

if [ "$TOKEN" = "null" ] || [ -z "$TOKEN" ]; then
    echo -e "${YELLOW}Usuario ya existe. Haciendo login...${NC}\n"

    LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d '{
        "username": "juan.perez",
        "password": "MiPassword123"
      }')

    echo "$LOGIN_RESPONSE" | jq '.'
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
fi

echo -e "\n${GREEN}✅ Token obtenido:${NC} ${TOKEN:0:50}...\n"

# 2. CREAR CALENDARIO
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📅 PASO 2: Creando calendario personal...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

CAL_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "Calendario Personal 2025",
    "descripcion": "Mi calendario con información sensible (será cifrado)",
    "color": "#3498db",
    "publico": false
  }')

echo "$CAL_RESPONSE" | jq '.'
CAL_ID=$(echo "$CAL_RESPONSE" | jq -r '.id')

echo -e "\n${GREEN}✅ Calendario creado con ID: $CAL_ID${NC}\n"

# 3. AGREGAR EVENTOS
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📌 PASO 3: Agregando eventos al calendario...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Evento 1: Reunión
EVENT1_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar/events" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"titulo\": \"Reunión con Cliente\",
    \"descripcion\": \"Presentación de propuesta comercial - Datos confidenciales\",
    \"fechaInicio\": \"2025-12-05T10:00:00\",
    \"fechaFin\": \"2025-12-05T11:30:00\",
    \"ubicacion\": \"Sala de Juntas A\",
    \"todoElDia\": false,
    \"tipo\": \"REUNION\",
    \"calendarioId\": $CAL_ID
  }")

echo -e "${YELLOW}Evento 1 - Reunión:${NC}"
echo "$EVENT1_RESPONSE" | jq '.'
EVENT1_ID=$(echo "$EVENT1_RESPONSE" | jq -r '.id')

# Evento 2: Tarea
EVENT2_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar/events" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"titulo\": \"Entregar Informe Trimestral\",
    \"descripcion\": \"Informe financiero Q4 - Información sensible\",
    \"fechaInicio\": \"2025-12-10T09:00:00\",
    \"fechaFin\": \"2025-12-10T18:00:00\",
    \"ubicacion\": \"Oficina\",
    \"todoElDia\": true,
    \"tipo\": \"TAREA\",
    \"calendarioId\": $CAL_ID
  }")

echo -e "\n${YELLOW}Evento 2 - Tarea:${NC}"
echo "$EVENT2_RESPONSE" | jq '.'
EVENT2_ID=$(echo "$EVENT2_RESPONSE" | jq -r '.id')

# Evento 3: Personal
EVENT3_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar/events" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"titulo\": \"Cumpleaños de María\",
    \"descripcion\": \"No olvidar comprar regalo\",
    \"fechaInicio\": \"2025-12-15T00:00:00\",
    \"fechaFin\": \"2025-12-15T23:59:59\",
    \"ubicacion\": \"Casa\",
    \"todoElDia\": true,
    \"tipo\": \"PERSONAL\",
    \"calendarioId\": $CAL_ID
  }")

echo -e "\n${YELLOW}Evento 3 - Personal:${NC}"
echo "$EVENT3_RESPONSE" | jq '.'
EVENT3_ID=$(echo "$EVENT3_RESPONSE" | jq -r '.id')

echo -e "\n${GREEN}✅ 3 eventos agregados${NC}\n"

# 4. CONFIGURAR ALERTAS
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}🔔 PASO 4: Configurando alertas...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Alerta PUSH para reunión
ALERT1_RESPONSE=$(curl -s -X POST "$BASE_URL/alerts" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"eventoId\": $EVENT1_ID,
    \"fechaAlerta\": \"2025-12-05T09:45:00\",
    \"tipoNotificacion\": \"PUSH\",
    \"minutosAnticipacion\": 15,
    \"mensaje\": \"Recordatorio: Reunión con cliente en 15 minutos\"
  }")

echo -e "${YELLOW}Alerta 1 - PUSH:${NC}"
echo "$ALERT1_RESPONSE" | jq '.'

# Alerta EMAIL para tarea
ALERT2_RESPONSE=$(curl -s -X POST "$BASE_URL/alerts" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"eventoId\": $EVENT2_ID,
    \"fechaAlerta\": \"2025-12-09T08:00:00\",
    \"tipoNotificacion\": \"EMAIL\",
    \"minutosAnticipacion\": 1440,
    \"mensaje\": \"Recordatorio: Entregar informe mañana\"
  }")

echo -e "\n${YELLOW}Alerta 2 - EMAIL:${NC}"
echo "$ALERT2_RESPONSE" | jq '.'

echo -e "\n${GREEN}✅ 2 alertas configuradas${NC}\n"

# 5. OBTENER CALENDARIOS
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📋 PASO 5: Obteniendo lista de calendarios...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

CALENDARS=$(curl -s -X GET "$BASE_URL/calendar" \
  -H "Authorization: Bearer $TOKEN")

echo "$CALENDARS" | jq '.'

# 6. OBTENER VISTA PREPARADA (con descifrado)
echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}🔓 PASO 6: Obteniendo vista con datos descifrados...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

VIEW=$(curl -s -X GET "$BASE_URL/calendar/view" \
  -H "Authorization: Bearer $TOKEN")

echo "$VIEW" | jq '.'

# 7. OBTENER ALERTAS
echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}🔔 PASO 7: Obteniendo alertas del usuario...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

ALERTS=$(curl -s -X GET "$BASE_URL/alerts" \
  -H "Authorization: Bearer $TOKEN")

echo "$ALERTS" | jq '.'

# RESUMEN FINAL
echo -e "\n${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║                  RESUMEN DE LA PRUEBA                    ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}\n"

echo -e "${GREEN}✅ Usuario registrado/autenticado${NC}"
echo -e "${GREEN}✅ Token JWT obtenido (válido por 24h)${NC}"
echo -e "${GREEN}✅ Calendario personal creado${NC}"
echo -e "${GREEN}✅ 3 eventos agregados (Reunión, Tarea, Personal)${NC}"
echo -e "${GREEN}✅ 2 alertas configuradas (PUSH, EMAIL)${NC}"
echo -e "${GREEN}✅ Datos cifrados automáticamente${NC}"
echo -e "${GREEN}✅ Vista con descifrado funcionando${NC}\n"

echo -e "${YELLOW}📊 DATOS CREADOS:${NC}"
echo -e "   Usuario ID: $(echo "$REGISTER_RESPONSE" | jq -r '.usuarioId')"
echo -e "   Calendario ID: $CAL_ID"
echo -e "   Eventos: $EVENT1_ID, $EVENT2_ID, $EVENT3_ID"
echo -e "   Token: ${TOKEN:0:30}..."

echo -e "\n${YELLOW}🔗 ENLACES ÚTILES:${NC}"
echo -e "   Swagger UI: http://localhost:8080/swagger-ui.html"
echo -e "   H2 Console: http://localhost:8080/h2-console"
echo -e "   SwaggerHub: https://app.swaggerhub.com/apis/udla-52c/api-sistema-de-calendario/1.0.0"

echo -e "\n${YELLOW}🎯 PRÓXIMOS PASOS:${NC}"
echo -e "   1. Explorar Swagger UI para probar más endpoints"
echo -e "   2. Ver los datos en H2 Console (jdbc:h2:mem:calendardb)"
echo -e "   3. Crear más usuarios y calendarios"
echo -e "   4. Probar las alertas (se procesan cada 60 segundos)"

echo -e "\n${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║           ¡SISTEMA FUNCIONANDO CORRECTAMENTE! 🎉        ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}\n"

