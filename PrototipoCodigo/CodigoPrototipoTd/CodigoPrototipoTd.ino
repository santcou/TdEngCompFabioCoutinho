#include <SPI.h>
//#include <Ethernet.h>
#include <PubSubClient.h>
#include <ESP8266WiFi.h> // Importa a Biblioteca ESP8266WiFi

#define D0    16
#define D1    5
#define D2    4
#define D3    0
#define D4    2
#define D5    14
#define D6    12
#define D7    13
#define D8    15
#define D9    3
#define D10   1


#define TOPICO_SUBSCRIBE "MQTTEnvia"     //tópico MQTT de escuta
#define TOPICO_PUBLISH   "MQTTRecebe"    //tópico MQTT de envio de informações para Broker
                                                   //IMPORTANTE: recomendamos fortemente alterar os nomes
                                                   //            desses tópicos. Caso contrário, há grandes
                                                   //            chances de você controlar e monitorar o NodeMCU
                                                   //            de outra pessoa.
#define ID_MQTT  "HomeAut"     //id mqtt (para identificação de sessão)
                               //IMPORTANTE: este deve ser único no broker (ou seja, 
                               //            se um client MQTT tentar entrar com o mesmo 
                               //            id de outro já conectado ao broker, o broker 
                               //            irá fechar a conexão de um deles).

#define LedBorker D2
#define LedWifi D4
#define LedMensagem D5

// WIFI
const char* SSID = "FABIO COUTINHO"; // SSID / nome da rede WI-FI que deseja se conectar
const char* PASSWORD = "santcou90"; // Senha da rede WI-FI que deseja se conectar
const int NIVEL_QoS = 1;
 
// MQTT
const char* BROKER_MQTT = "m14.cloudmqtt.com"; //URL do broker MQTT que se deseja utilizar
int BROKER_PORT = 12114; // Porta do Broker MQTT

//const char* BROKER_MQTT = "10.0.0.8"; //URL do broker MQTT que se deseja utilizar
//int BROKER_PORT = 1883; // Porta do Broker MQTT

const char* USER_MQTT = "mqrybazf";
const char* PASSWORD_MQTT = "gEVFOPGKbx6R";

bool publicar = false;
unsigned long  tempo;
unsigned long ult_tempo = 0;

void verificaConexaoBroker();
void verificaConexaoWifi();
void publicaCoordenadas(void);
void EnviaEstadoOutputMQTT(void);
void ativarPublicacao(void);
void acendeLed(void);
void apagaLed(void);
void piscaLedConexao(void);
void callback(char* topic, byte* payload, unsigned int length) {
  // handle message arrived
}




//Variáveis e objetos globais
WiFiClient espClient; // Cria o objeto espClient
PubSubClient client(BROKER_MQTT, BROKER_PORT, callback, espClient);

void setup()
{
  Serial.begin(115200);
  Serial.println("REINICIEI PORRA!!!");
  pinMode(LedBorker, OUTPUT);
  pinMode(LedWifi, OUTPUT);
  pinMode(LedMensagem, OUTPUT);
  attachInterrupt(0,ativarPublicacao,RISING);
  client.subscribe(TOPICO_SUBSCRIBE,NIVEL_QoS);

  if (WiFi.status() == WL_CONNECTED)
       return;
        
   WiFi.begin(SSID, PASSWORD); // Conecta na rede WI-FI
    
   while (WiFi.status() != WL_CONNECTED) 
   {
       delay(100);
       piscaLedConexao();
       Serial.print(".");
   }


 
   Serial.println();
   Serial.print("Conectado com sucesso na rede ");
   Serial.print(SSID);
   Serial.println("IP obtido: ");
   Serial.println(WiFi.localIP());
   digitalWrite(LedWifi,HIGH);
   
   verificaConexaoWifi(); 
   verificaConexaoBroker();
}

void verificaConexaoWifi()
{
  if (WiFi.status() == WL_CONNECTED)
       return;
        
   WiFi.begin(SSID, PASSWORD); // Conecta na rede WI-FI
    
   while (WiFi.status() != WL_CONNECTED) 
   {
       delay(100);
       piscaLedConexao();
       Serial.print(".");
   }
   Serial.println();
   Serial.print("Conectado com sucesso na rede ");
   Serial.print(SSID);
   Serial.println("IP obtido: ");
   Serial.println(WiFi.localIP());
   digitalWrite(LedWifi,HIGH);
}

void verificaConexaoBroker()
{
  if (client.connect("arduinoClient", USER_MQTT, PASSWORD_MQTT))
   //if (client.connect("arduinoClient"),NIVEL_QoS)
  {
    Serial.println("Conectado com o Broker");
    acendeLed();
  }
  else
  {
    client.disconnect();
    Serial.println("Desconectado com o Broker");
    apagaLed();
    verificaConexaoWifi();
  }
}

void piscaLedConexao(void)
{

  digitalWrite(LedWifi,HIGH);
  delay(200);
  digitalWrite(LedWifi,LOW);
  delay(200);
}

void publicaCoordenadas(void)
{
  if (client.connected()) 
  {
    client.publish(TOPICO_PUBLISH,"AV BRASILIA,1200,SANTA LUZIA,SÃO BENEDITO,NÃO É COMÉRCIO,71,FABIO COUTINHO DOS SANTOS");
    Serial.println("Publicou");
    digitalWrite(LedMensagem,HIGH);
    delay(1000);
    digitalWrite(LedMensagem,LOW);
  }
  publicar = false;
  //apagaLed();
}

void ativarPublicacao(void){
  publicar = true;
}

void acendeLed(void)
{
  digitalWrite(LedBorker,HIGH);
  Serial.print("LED ACESO");
}

void apagaLed(void)
{
  digitalWrite(LedBorker,LOW);
  Serial.print("LED APAGADO");
}


void loop()
{
  client.loop();
  verificaConexaoBroker();

  /*tempo = millis();
  if((tempo - ult_tempo) >= 10000)
  {
    ult_tempo = tempo;
    Serial.println("2 segundos");
    verificaConexaoBroker();
  }*/
  
  if(publicar){
    //acendeLed();
    publicaCoordenadas();
    delay(2000);
  }
  //publicaCoordenadas();
}
