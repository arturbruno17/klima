<h1 align="center">Klíma</h1>
<p align="center">Aplicativo que disponibiliza previsões sobre o clima de determinado local. </p>

<p align="center">
   <img src="https://img.shields.io/static/v1?label=jetpack-compose&message=1.3.0-alpha01&color=2EC781&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=ktor-client&message=2.0.3&color=F26636&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=kotlinx-serialization&message=1.3.3&color=7F52FF&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=datastore-preferences&message=1.0.0&color=2EC781&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=kotlinx-datetime&message=0.4.0&color=7F52FF&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=hilt&message=2.42&color=2196F3&style=flat-square"/>
   <img src="https://img.shields.io/static/v1?label=material3&message=1.0.0-alpha14&color=FDD0CF&style=flat-square"/>
</p>

<p align="center">
 <a href="#-sobre">Sobre</a> •
 <a href="#-screenshots">Screenshots</a> • 
 <a href="#-bibliografia">Bibliografia</a> •
 <a href="#-contribuição">Contribuição</a>
</p>

# 📱 Build

Você irá precisar da APIKEY válida para rodar esse projeto, você pode pegar a sua em:

- Caso já possua login na openweather [PEGUE_SUA_KEY](https://home.openweathermap.org/api_keys)
- Caso ainda não tenha uma conta crie a sua: [Crie Sua Conta](https://home.openweathermap.org/users/sign_up)

Você precisará adicionar ao arquivo gerado pela IDE `local.properties` a seguinte informação.

```markdown
OPENWEATHER_API_KEY=SUA_KEY
```

# 📜 Sobre

Aplicativo de previsões climáticas feito com Android nativo. Possui uma tela de dashboard onde é possível pesquisar por um local e receber previsões
climáticas sobre o clima atual e das próximas horas. Também possui uma tela de calendário, onde mostra a previsão climática dos próximos 7 dias e só é
liberada depois que você faz a pesquisa. Além disso, você pode alterar, nas configurações, o sistema de unidade usado nas medidas, podendo escolher
entre métrico (°C e m/s) e imperial (°F e mph).

* **Jetpack Compose**: O Jetpack Compose é um kit de ferramentas moderno do Android para criar IUs nativas. Ele simplifica e acelera o desenvolvimento
  da IU no Android.

* **Ktor Client**: O Ktor inclui um cliente HTTP assíncrono multiplataforma, que permite fazer solicitações e lidar com respostas, estender sua
  funcionalidade com plugins, como autenticação, serialização JSON e assim por diante.

* **kotlinx-serialization**: O kotlinx-serialization consiste em um plug-in do compilador, que gera código para classes serializáveis, biblioteca de
  tempo de execução com API de serialização central e bibliotecas de suporte com vários formatos de serialização. No caso do aplicativo, foi usado
  apenas o **kotlinx-serialization-json**.

* **Jetpack DataStore**: uma solução de armazenamento de dados que permite armazenar pares de chave-valor ou objetos tipados com buffers de protocolo.
  O DataStore usa corrotinas e fluxo do Kotlin para armazenar dados de forma assíncrona, consistente e transacional.

* **kotlinx-datetime**: Uma biblioteca Kotlin multiplataforma para trabalhar com data e hora.

* **Hilt**: O Hilt fornece uma maneira padrão de incorporar a injeção de dependência do Dagger em um aplicativo Android.

* **Material 3**: A versão mais recente do Material Design inclui recursos de personalização e acessibilidade que colocam as pessoas no centro.

# 📱 Screenshots

<p align="center">
  <img src="https://i.ibb.co/vvh66ZZ/Screenshot-20220708-084231.png" width="30%"/>
  <img src="https://i.ibb.co/Qkv98Cq/Screenshot-20220708-084248.png" width="30%"/>
  <img src="https://i.ibb.co/tpB2RdJ/Screenshot-20220708-084304.png" width="30%"/>
</p>

# 📚 Bibliografia

Nesta seção, você encontrará vários links e recursos que falam acerca das bibliotecas e extras utilizados no projeto.

|  Biblioteca   |  Link    |
|---	|---	|
|   Jetpack Compose    |   https://developer.android.com/jetpack    |
|   Ktor Client    |   https://ktor.io/docs/create-client.html#configure-client    |
|   kotlinx-serialization    |   https://github.com/Kotlin/kotlinx.serialization    |
|   Jetpack DataStore    |   https://developer.android.com/topic/libraries/architecture/datastore?hl=pt-br    |
|   kotlinx-datetime    |   https://github.com/Kotlin/kotlinx-datetime    |
|   Hilt    |   https://developer.android.com/training/dependency-injection/hilt-android?hl=pt-br#inject-interfaces    |
|   Material 3    |   https://m3.material.io/    |

# 🤝 Contribuição

O app foi criado e testado em um dispositivo físico, Redmi Note 9s, e em dois emuladores, um com API 28 e o outro com API 31, mas é disponível para
qualquer um que queira contribuir.

Caso tenha alguma ideia de como melhorar o app, realize os seguintes passos:

1. Para contribuir, basta fazer um fork.
   (<https://github.com/arturbruno17/klima/fork>)

2. Crie uma branch para sua modificação
   (`git checkout -b feature/fooBar`)

3. Faça o commit
   (`git commit -am "Add some fooBar"`)

4. Push
   (`git push origin feature/fooBar`)

5. Crie um novo *Pull Request*
