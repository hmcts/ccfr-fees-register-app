#!/bin/sh

#BASE_URL=$1
BASE_URL=https://pr-141-fees-register-api-preview.service.core-compute-preview.internal
ADMIN_TOKEN="replace with fees admin token"
{
    read #skip header
    while IFS=, read -r CODE OTHER
    do
        printf "Invoking delete endpoint for the code:%s \n" $CODE
        curl -kX DELETE $BASE_URL/fees-register/fees/$CODE -H "Authorization: $ADMIN_TOKEN"
    done
} < fees_to_delete.csv

