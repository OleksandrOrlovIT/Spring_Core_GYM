# Spring_Core_GYM

<h2>Requirements</h2>
In order to build application you need to add .env file and fill next information inside it.

KEY_STORE_PASSWORD=<br/>
TRUST_STORE_PASSWORD=<br/>

POSTGRES_DB=<br/>
POSTGRES_USER=<br/>
POSTGRES_PASSWORD=<br/>

RABBITMQ_USER=<br/>
RABBITMQ_PASSWORD=<br/>

TOKEN_SIGNING_KEY=<br/>

<h2>Commands</h2>
<h3>Commands to build application with docker running on the machine</h2>

mvn clean package -DskipTests=true

docker-compose up --build

<h3>Commands to test application and create jacoco coverage</h2>

mvn clean install verify


