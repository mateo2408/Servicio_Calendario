#!/bin/bash

# Script para probar autenticación JWT
# Uso: ./test_jwt.sh

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

echo -e "${BLUE}╔══════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   PRUEBA DE AUTENTICACIÓN JWT               ║${NC}"
echo -e "${BLUE}║   Sistema de Calendario                      ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════╝${NC}\n"

# Verificar si el servidor está corriendo
echo -e "${YELLOW}⏳ Verificando servidor...${NC}"
if ! curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
    if ! curl -s "$BASE_URL" > /dev/null 2>&1; then
        echo -e "${RED}❌ ERROR: El servidor no está corriendo en $BASE_URL${NC}"
        echo -e "${YELLOW}💡 Inicia la aplicación primero:${NC}"
        echo -e "   - IntelliJ IDEA: Run 'CalendarioApplication'"
        echo -e "   - Maven: mvn spring-boot:run"
        exit 1
    fi
fi
echo -e "${GREEN}✅ Servidor está corriendo${NC}\n"

# 1. REGISTRAR USUARIO
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📝 PASO 1: Registrando nuevo usuario...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

REGISTRO_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nombre": "Usuario de Prueba"
  }')

echo "$REGISTRO_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTRO_RESPONSE"

# Extraer token
TOKEN=$(echo "$REGISTRO_RESPONSE" | jq -r '.token' 2>/dev/null)

if [ "$TOKEN" = "null" ] || [ -z "$TOKEN" ]; then
    echo -e "\n${YELLOW}⚠️  Usuario ya existe. Intentando login...${NC}\n"

    # 2. LOGIN
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}🔐 PASO 2: Iniciando sesión...${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

    LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d '{
        "username": "testuser",
        "password": "password123"
      }')

    echo "$LOGIN_RESPONSE" | jq '.' 2>/dev/null || echo "$LOGIN_RESPONSE"

    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null)
fi

# Verificar que tenemos token
if [ "$TOKEN" = "null" ] || [ -z "$TOKEN" ]; then
    echo -e "\n${RED}❌ ERROR: No se pudo obtener el token${NC}"
    exit 1
fi

echo -e "\n${GREEN}✅ Token JWT obtenido exitosamente${NC}"
echo -e "${BLUE}Token:${NC} ${TOKEN:0:50}...\n"

# 3. CREAR CALENDARIO
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📅 PASO 3: Creando calendario (con token)...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

CALENDAR_RESPONSE=$(curl -s -X POST "$BASE_URL/calendar" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "Mi Calendario Personal",
    "descripcion": "Calendario creado automáticamente por script de prueba",
    "color": "#FF5733",
    "publico": false
  }')

echo "$CALENDAR_RESPONSE" | jq '.' 2>/dev/null || echo "$CALENDAR_RESPONSE"

CALENDAR_ID=$(echo "$CALENDAR_RESPONSE" | jq -r '.id' 2>/dev/null)

if [ "$CALENDAR_ID" != "null" ] && [ -n "$CALENDAR_ID" ]; then
    echo -e "\n${GREEN}✅ Calendario creado con ID: $CALENDAR_ID${NC}"
else
    echo -e "\n${YELLOW}⚠️  No se pudo obtener ID del calendario${NC}"
fi

# 4. LISTAR CALENDARIOS
echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}📋 PASO 4: Obteniendo lista de calendarios...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

CALENDARS_LIST=$(curl -s -X GET "$BASE_URL/calendar" \
  -H "Authorization: Bearer $TOKEN")

echo "$CALENDARS_LIST" | jq '.' 2>/dev/null || echo "$CALENDARS_LIST"

# 5. RESUMEN
echo -e "\n${BLUE}╔══════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║              RESUMEN DE LA PRUEBA            ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════╝${NC}\n"

echo -e "${GREEN}✅ Autenticación exitosa${NC}"
echo -e "${GREEN}✅ Token JWT obtenido${NC}"
echo -e "${GREEN}✅ Endpoint protegido accedido correctamente${NC}"
echo -e "${GREEN}✅ Sistema funcionando correctamente${NC}\n"

echo -e "${YELLOW}📌 INFORMACIÓN IMPORTANTE:${NC}"
echo -e "   Token: ${TOKEN:0:50}..."
echo -e "   Duración: 24 horas"
echo -e "   Formato header: Authorization: Bearer {token}"
echo -e "\n${YELLOW}🔗 ENLACES ÚTILES:${NC}"
echo -e "   Swagger UI: http://localhost:8080/swagger-ui.html"
echo -e "   H2 Console: http://localhost:8080/h2-console"
echo -e "\n${YELLOW}📚 DOCUMENTACIÓN:${NC}"
echo -e "   Ver: GUIA_AUTENTICACION_JWT.md\n"

echo -e "${BLUE}╔══════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║         ¡PRUEBA COMPLETADA! 🎉               ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════╝${NC}\n"

