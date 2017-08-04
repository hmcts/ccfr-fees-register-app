#!/usr/bin/env bash

set -e

if [ -z "$FEES_REGISTER_DB_USERNAME" ] || [ -z "$FEES_REGISTER_DB_PASSWORD" ]; then
  echo "ERROR: Missing environment variable. Set value for both 'FEES_REGISTER_DB_USERNAME' and 'FEES_REGISTER_DB_PASSWORD'."
  exit 1
fi

# Create role and database
psql -v ON_ERROR_STOP=1 --username postgres --set USERNAME=$FEES_REGISTER_DB_USERNAME --set PASSWORD=$FEES_REGISTER_DB_PASSWORD <<-EOSQL
  CREATE USER :USERNAME WITH PASSWORD ':PASSWORD';

  CREATE DATABASE fees_register
    WITH OWNER = :USERNAME
    ENCODING = 'UTF-8'
    CONNECTION LIMIT = -1;
EOSQL
