#!/bin/bash

# =============================================================================
# Script de Ejecución de Tests - Servicio Calendario
# =============================================================================
#
# Uso:
#   ./run_tests.sh unit           # Ejecutar pruebas unitarias
#   ./run_tests.sh integration    # Ejecutar pruebas de integración
#   ./run_tests.sh contract       # Ejecutar pruebas de contrato
#   ./run_tests.sh all            # Ejecutar todos los tests
#   ./run_tests.sh coverage       # Ejecutar con reporte de cobertura
#   ./run_tests.sh clean          # Limpiar y solucionar problema Lombok
#
# =============================================================================

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Banner
echo -e "${BLUE}"
echo "=========================================="
echo "   Tests - Servicio Calendario"
echo "=========================================="
echo -e "${NC}"

# Función para mostrar uso
show_usage() {
    echo -e "${YELLOW}Uso:${NC}"
    echo "  ./run_tests.sh unit           # Pruebas unitarias (rápidas)"
    echo "  ./run_tests.sh integration    # Pruebas de integración (requiere Docker)"
    echo "  ./run_tests.sh contract       # Pruebas de contrato"
    echo "  ./run_tests.sh all            # Todos los tests"
    echo "  ./run_tests.sh coverage       # Con reporte de cobertura"
    echo "  ./run_tests.sh clean          # Limpiar y solucionar Lombok"
    echo ""
}

# Función para verificar Docker
check_docker() {
    if ! docker ps &> /dev/null; then
        echo -e "${RED}❌ Docker no está corriendo o no está instalado${NC}"
        echo -e "${YELLOW}Por favor, inicia Docker Desktop antes de ejecutar tests de integración${NC}"
        exit 1
    else
        echo -e "${GREEN}✅ Docker está corriendo${NC}"
    fi
}

# Función para limpiar y solucionar Lombok
clean_lombok() {
    echo -e "${YELLOW}🧹 Limpiando proyecto y repositorio Lombok...${NC}"

    # Limpiar target
    echo "  → Limpiando directorio target..."
    rm -rf target/

    # Limpiar repositorio Lombok en directorio home del usuario
    echo "  → Limpiando repositorio Maven de Lombok..."
    rm -rf $HOME/.m2/repository/org/projectlombok/

    # Limpiar proyecto Maven
    echo "  → Ejecutando mvn clean..."
    mvn clean

    echo -e "${GREEN}✅ Limpieza completada${NC}"
    echo ""
    echo -e "${YELLOW}Ahora intenta:${NC}"
    echo "  mvn install -DskipTests"
    echo "  mvn test -Dtest=\"org.example.unit.**\""
}

# Función para ejecutar tests unitarios
run_unit_tests() {
    echo -e "${BLUE}🧪 Ejecutando Pruebas Unitarias...${NC}"
    echo "  → Sin base de datos, solo mocks en memoria"
    echo "  → Tiempo estimado: 5-10 segundos"
    echo ""

    mvn test -Dtest="org.example.unit.**"

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Pruebas unitarias completadas exitosamente${NC}"
    else
        echo -e "${RED}❌ Algunas pruebas unitarias fallaron${NC}"
        exit 1
    fi
}

# Función para ejecutar tests de integración
run_integration_tests() {
    echo -e "${BLUE}🔄 Ejecutando Pruebas de Integración...${NC}"
    echo "  → Con base de datos real (PostgreSQL en TestContainers)"
    echo "  → Tiempo estimado: 20-40 segundos"
    echo ""

    # Verificar Docker
    check_docker

    mvn test -Dtest="org.example.integration.**"

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Pruebas de integración completadas exitosamente${NC}"
    else
        echo -e "${RED}❌ Algunas pruebas de integración fallaron${NC}"
        exit 1
    fi
}

# Función para ejecutar tests de contrato
run_contract_tests() {
    echo -e "${BLUE}🌐 Ejecutando Pruebas de Contrato...${NC}"
    echo "  → Con WireMock simulando APIs externas"
    echo "  → Tiempo estimado: 10-15 segundos"
    echo ""

    mvn test -Dtest="org.example.contract.**"

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Pruebas de contrato completadas exitosamente${NC}"
    else
        echo -e "${RED}❌ Algunas pruebas de contrato fallaron${NC}"
        exit 1
    fi
}

# Función para ejecutar todos los tests
run_all_tests() {
    echo -e "${BLUE}🚀 Ejecutando TODOS los Tests...${NC}"
    echo "  → Unitarias + Integración + Contrato"
    echo "  → Tiempo estimado: 40-60 segundos"
    echo ""

    # Verificar Docker para tests de integración
    check_docker

    mvn clean test

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Todos los tests completados exitosamente${NC}"
    else
        echo -e "${RED}❌ Algunos tests fallaron${NC}"
        exit 1
    fi
}

# Función para ejecutar tests con cobertura
run_coverage() {
    echo -e "${BLUE}📊 Ejecutando Tests con Reporte de Cobertura...${NC}"
    echo ""

    check_docker

    mvn clean test jacoco:report

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Tests completados con reporte de cobertura${NC}"
        echo ""
        echo -e "${YELLOW}📈 Ver reporte en:${NC}"
        echo "  target/site/jacoco/index.html"
        echo ""

        # Intentar abrir el reporte automáticamente según el sistema operativo
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS
            open target/site/jacoco/index.html 2>/dev/null
        elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
            # Linux
            xdg-open target/site/jacoco/index.html 2>/dev/null
        elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
            # Windows
            start target/site/jacoco/index.html 2>/dev/null
        fi
    else
        echo -e "${RED}❌ Error al generar reporte de cobertura${NC}"
        exit 1
    fi
}

# Parsear argumentos
case "$1" in
    unit|u)
        run_unit_tests
        ;;
    integration|i)
        run_integration_tests
        ;;
    contract|c)
        run_contract_tests
        ;;
    all|a)
        run_all_tests
        ;;
    coverage|cov)
        run_coverage
        ;;
    clean|cl)
        clean_lombok
        ;;
    help|h|--help)
        show_usage
        ;;
    *)
        echo -e "${RED}❌ Opción inválida: $1${NC}"
        echo ""
        show_usage
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}✨ Proceso completado${NC}"

