# UdescBitTorrent




<details>
<summary>Comandos para executar os serviços</summary>

Comandos de clientes do peer
```shell
#Busca o próximo arquivo faltante
curl --location 'http://localhost:8002/client/'

#Busca todos os arquivos faltantes
curl --location 'http://localhost:8002/client/all'
```
Comando de servidor do peer
```shell
#Devolve o arquivo 1-file.txt caso exista
curl --location 'http://localhost:8002/server/1-file.txt'
```

Comandos do tracker
```shell
#Devolve a lista de peers e seus respectivos arquivos
curl --location 'http://localhost:8004'

#Adiciona o peer que realizou a requisição a lista de peers com os arquivos especificos
curl --location 'http://localhost:8004' \
--header 'Content-Type: application/json' \
--data '[
"1-file.txt",
"3-file.txt",
"2-file.txt"
]'
```
</details>
