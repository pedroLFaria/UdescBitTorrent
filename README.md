# UdescBitTorrent

**Requisitos**
- apache-maven-3.8.3
- Java version: 18.0.1

**Instalar os pacotes**
```console
$ mvn clean install
```

**Comando para execuar o jar**

*Modificar os parâmetros*

```console
# Executar o tracker
java -Dprofile="tracker" -jar target/my-app-1.0-SNAPSHOT.jar

#Executar um peer
java -Dprofile="peer" -D"peer.thread.sleep-time"="5" -D"tracker.url"="http://localhost:8004" -jar target/my-app-1.0-SNAPSHOT.jar
```


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
|```shell
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
