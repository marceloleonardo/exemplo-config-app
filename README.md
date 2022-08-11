# exemplo-config-app

O Spring (não apenas o spring boot, mas todo o ecosistema) é sem dúvida umas das melhores tecnologias 
do tipo "canivete suíço" já criadas. Quem é dev Java e quer dar um up na carreira, ser mais produtivo
e ganhar estrelas com os empregadores e clientes, com toda certeza tem que aprender e lidar com esse 
carinha.

Falando bem resumidamente e sem querer esgotar o assunto, basicamente os famosos esterótipos do Spring 
fazem com que, através de anotações, suas classes ganhem superpoderes.

Outra caracteristica interessante é a possibilidade de parametrizar seu app, sem a necessidade de ter
de alterar um codigo só por causa de dados dinamicos. Isso é fantastico.

Estou falando do nosso querido application.properties.

Vou trazer aqui uma dica legal que com certeza é do conhecimento de alguns colegas dev´s porém acredito também não
ser do conhecimento de outros. Essa dica não tem a intenção de ser a mais nova maravilha do mundo e nem tão pouco
é a mais indicada para todos os casos. É apenas uma dica para fins de propagação de conhecimento. No final, você é 
quem vai analisar se ela é util para seus projetos ou não.

Suponhamos que seu app tenha que consumir uma API Rest externa, mas como você está em fase de homologação precisa
testar seu app em ambiente de desenvolvimento e homologação e por causa disso, a url da API que você precisa consumir, os
dados de autenticação e alguns endPoints são diferentes entre os ambientes. Como eu poderia agilizar minha vida sem fixar os
dados da API no codigo e não ter de ficar refatorando a todo momento por causa disso?

Bora lá?

Primeiro, vamos fixar algumas informações no nosso arquivo de propriedades:

# Dados da API
hostApi = https://hml.cliente.sytes.net
userApi = user1
passwordApi = 123456
endPointAuth = /apiestoque/v1/oauth/token
endPointCategorias = /apiestoque/v1/categorias

O que fizemos foi fixar os dados básicos de acesso e autenticação da api no arquivo de propriedades. Dessa forma,
posso instruir meu codigo java a buscar essas informações e usá-las para ter acesso a API:

    // Inicio do codigo omitido
    ...

	public static void main(String[] args) {
		SpringApplication.run(ExemploConfigAppApplication.class, args).close();
	}

	@Component
	public static class Runner implements ApplicationRunner {

		@Value("${hostApi}")
		String hostApiGrupoNc;
		  
		@Value("${userApi}")
		String userApi;
	
		@Value("${passwordApi}")
		String passwordApi;

		@Value("${endPointAuth}")
		String endPointAuth;

		@Value("${endPointCategorias}")
        String endPointCategorias;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			System.out.println("Buscando dados de acesso da API");
			System.out.println("URL     : " + this.hostApi);
			System.out.println("Usuario : " + this.userApi);
			System.out.println("Senha   : " + this.passwordApi);
			System.out.println("EndPoint Token: " + this.endPointAuth);
			System.out.println("EndPoint Categorias: " + this.endPointCategorias);
		}
    }

Dessa forma, basta eu utilizar as informações fixadas no arquivo de configuração e pronto !! 
Não vou precisar alterar minha app caso os dados de acesso da API mude. 

Lindo né?! Só que não !!

Esse codigo tá muito feio. Essas propriedades soltas dentro da classe principal não estão nem um pouco
amigáveis. É claro, fiz essa implementação na classe principal só para efeitos de teste mesmo. 

Mas lembra quando eu escrevi acima que com o Spring suas classes ganham superpoderes? 

Pois é! Vamos dar uma melhorada nesse codigo? Que tal criarmos uma classe de configuração especifica para coletar
os dados de acesso da API, e por meio da injeção de dependencias, eu poder usar essa classe em qualquer lugar da 
minha app?

@Configuration
public class ApiFactoryConfig {

    @Value("${hostApi}")
    String hostApi;
      
    @Value("${userApi}")
    String userApi;

    @Value("${passwordApi}")
    String passwordApi;

    @Value("${endPointAuth}")
    String endPointAuth;

    @Value("${endPointCategorias}")
    String endPointCategorias;

    // Getters and Setters below ...

Ou seja, criamos uma classe de nome "ApiFactoryConfig" e colocamos a anotação "@Configuration", dizendo para o Spring
que essa é uma classe de configuração e podemos, entre outras coisas, utiliza-la via injeção de dependencias.

No caso, após a criação dessa classe de configuração, poderiamos refatorar nossa classe principal :

	@Component
	public static class Runner implements ApplicationRunner {

        @Autowired
		private ApiFactoryConfig config;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			System.out.println("Buscando dados da API");
			System.out.println("URL     : " + config.getHostApi());
			System.out.println("Usuario : " + config.getUserApi());
			System.out.println("Senha   : " + config.getPasswordApi());
			System.out.println("EndPoint Token: " + config.getEndPointAuth());
			System.out.println("EndPoint Categorias: " + config.getEndPointCategorias());
		}
	}

Por intermédio da anotação "@Autowired", posso injetar a minha classe de configuração e utilizar os atributos dela,
na classe principal (ou em qualquer outra) deixando meu código mais limpo e desacoplado.

Agora ficou bonitão, né?! Nem a pau, Juvenal !!

Vamos melhorar nosso codigo mais um pouco !

Nós criamos a classe de configuração e encapsulamos os atributos que são alimentados via application.properties
nela. Perfeito! Só que aquele código continua feio por conta daquelas anotações "@Value" em todos os 
atributos. Além de repetirmos a anotação em todos os atributos, tenho que escrever o nome do atributo novamente,
pois eu já tinha escrito no application.properties. Podemos melhorar isso e eliminar linhas de codigo desnecessárias,
além de tirar mais um coelho da cartola do Spring.

Existe uma outra anotação que fornece a minha classe de configuração mais um superpoder muito legal:

@Configuration
@ConfigurationProperties(prefix = "dadosapi")
public class ApiFactoryConfig {

    private String hostApi;
    private String userApi;
    private String passwordApi;
    private String endPointAuth;
    private String endPointCategorias;

    // Getters and Setters below ...
               ...

Olha que legal! Através da anotação "@ConfigurationProperties", posso criar um prefixo dentro do
application.properties e agrupar atributos especificos de uma configuração:

// application.properties
# Dados da API
dadosapi.hostApi = https://hml.cliente.sytes.net
dadosapi.userApi = user1
dadosapi.passwordApi = 123456
dadosapi.endPointAuth = /apiestoque/v1/oauth/token
dadosapi.endPointCategorias = /apiestoque/v1/categorias

E o melhor: Caso os atributos da minha classe tenham o mesmo nome das propriedades que fixei no 
application.properties, o spring faz o mapeamento dos atributos automaticamente, deixando nosso 
codigo mais limpo um pouco.

E a nossa classe principal, continua a mesma:

	@Component
	public static class Runner implements ApplicationRunner {

        @Autowired
		private ApiFactoryConfig config;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			System.out.println("Buscando dados da API");
			System.out.println("URL     : " + config.getHostApi());
			System.out.println("Usuario : " + config.getUserApi());
			System.out.println("Senha   : " + config.getPasswordApi());
			System.out.println("EndPoint Token: " + config.getEndPointAuth());
			System.out.println("EndPoint Categorias: " + config.getEndPointCategorias());
		}
	}

Agora pronto, né? Hummm, falta um detalhe ainda....

No spring, temos o conceito de configurações externalizadas, onde eu posso utilizar algumas formas de passar dados
de configuração para minha app, em tempo de execução. Você pode fazer isso passando argumentos por linha de comando,
utilizar variaveis de ambiente, etc...

No nosso caso em especial, fizemos a configuração via application.properties e existem maneiras de configurar o 
spring para buscar tais informações separando perfis de "DEV" e "PROD" por exemplo. Vou deixar isso como desafio
para vocês.

Esse app que construímos não faz nada além de mostrar os dados que fixamos no application.properties, mas serve de exemplo
para que você possa utilizar em seus projetos.

No meu caso, ultimamente tenho trabalhando quase que exclusivamente com construção de bot´s para integração de dados e 
informações entre sistemas e bancos de dados. E Nesse caso, pensando nesse app como um futuro robô, assim que voce fizer
o build de uma release e for subir em algum servidor Linux, cluster, maquina stand-alone ou algo do tipo, basta copiar o arquivo
application.properties no mesmo local do app e pronto. Preste atenção em detalhes importantes e informações sigilosas e perigosas
de se colocar em um application.properties, como senhas de acesso descriptografadas por exemplo.

Bem pessoal, era isso que eu queria compartilhar com vocês. Espero que essa humilde contribuição seja 
util para alguém e para seus projetos de algum modo.

Forte abraços a todos os irmãos e irmãs da comunidade Java.

