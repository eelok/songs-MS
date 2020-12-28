#-o /dev/null -s -w "%{http_code}\n"

TOKEN=$(curl -H "Content-type: application/json" -X POST -d '{ "userId":"mmuster", "password":"pass1234" }' http://localhost:8080/rest/auth)

curl -o /dev/null -s -w "%{http_code}\n" \
  -H "Authorization: ${TOKEN}" \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -X PUT -d '{
    "id": "5",
    "ownerId": "mmuster",
    "isPrivate": false,
    "name": "MaximesPrivate New Updated"
  }' \
  "http://localhost:8080/songLists/5"