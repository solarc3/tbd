\set ON_ERROR_STOP on

\echo "Paso 1: Crear la estructura"
\i dbCreate.sql
\echo ">>>"
\echo ""

\echo "Paso 2: Cargar datos"
\i loadData.sql
\echo ">>>"
\echo ""

\echo "Paso 3: Ejecutar Querys"
\i runStatements.sql
\echo ">>>"
\echo ""

\echo "executed OK"