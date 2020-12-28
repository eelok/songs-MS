#-o /dev/null -s -w "%{http_code}\n"

TOKEN=$(curl -H "Content-type: application/json" -X POST -d '{ "userId":"mmuster", "password":"pass1234" }' http://localhost:8080/rest/auth)

curl -o /dev/null -s -w "%{http_code}\n" \
  -H "Authorization: ${TOKEN}" \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -X POST -d '{
    "isPrivate": true,
    "name": "MaximesPrivate",
    "songs": [
      {
        "id": 5,
        "title": "We Built This City",
        "artist": "Starship",
        "label": "Grunt/RCA",
        "released": 1985
      },
      {
        "id": 4,
        "title": "Sussudio",
        "artist": "Phil Collins",
        "label": "Virgin",
        "released": 1985
      }
    ]
  }' \
  http://localhost:8080/songLists