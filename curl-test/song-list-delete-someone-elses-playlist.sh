TOKEN=$(curl -H "Content-type: application/json" -X POST -d '{ "userId":"mmuster", "password":"pass1234" }' http://localhost:8080/rest/auth)

curl -o /dev/null -s -w "%{http_code}\n" \
-H "Authorization: ${TOKEN}" \
-X DELETE 'http://localhost:8080/songLists/1'