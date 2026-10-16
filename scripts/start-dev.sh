#!/usr/bin/env bash

set -euo pipefail

if [[ -z "${DB_URL:-}" || -z "${DB_USERNAME:-}" || -z "${DB_PASSWORD:-}" ]]; then
  cat >&2 <<'EOF'
Faltan variables de conexión a SQL Server.

Define DB_URL, DB_USERNAME y DB_PASSWORD antes de ejecutar este script.
EOF
  exit 1
fi

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
service_dir="$root_dir/service"
frontend_dir="$root_dir/crudFront"

cleanup() {
  kill "$service_pid" "$frontend_pid" 2>/dev/null || true
  wait "$service_pid" "$frontend_pid" 2>/dev/null || true
}

if [[ ! -x "$frontend_dir/node_modules/.bin/ng" ]]; then
  echo "Instalando dependencias del frontend..."
  (cd "$frontend_dir" && npm ci)
fi

echo "Iniciando servicio en http://localhost:8080..."
(cd "$service_dir" && ./gradlew bootRun) &
service_pid=$!

echo "Iniciando frontend en http://localhost:4200..."
(cd "$frontend_dir" && npm start) &
frontend_pid=$!

trap cleanup EXIT INT TERM

echo "Ambos proyectos se están iniciando. Presiona Ctrl+C para detenerlos."
wait "$service_pid" "$frontend_pid"
