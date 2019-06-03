# Java Android app made for a technical test


### Architecture, language and tools used to build this app

- MVI (Model View Intent)
- Clean architecture
- Dagger 2 
- RxAndroid
- SOLID principles
- Retrofit
- Espresso 
- JUnit 
- LifeCycle
- Custom views (to encapsulate Glide for instance([shortcut link](https://github.com/skategui/argent-tech-test/tree/master/app/src/main/java/agis/guillaume/argent/ui/custom) ).


### The Subject 

Create a crypto app with 2 views that displays some info about the ETH balance and the ERC 20 tokens for a given account addr.

### My App workflow

### Set the API Key and the Account addr

Go into the [config/variables.properties](https://github.com/skategui/argent-tech-test/blob/master/config/variables.properties) and set the `etherscan_api_key` and `etn_account_addr` you wish to use.
Both properties are needed to make the app work propertly. Set them before building the app.

#### The [Account Activity](https://github.com/skategui/argent-tech-test/tree/master/app/src/main/java/agis/guillaume/argent/ui/account)
 Display the balance of the account address provided in the config, in ETH, with the aggregation of all the ERC 20 tokens, also in ETH.

 While loading those information for the server and/or locally, it displays a loading animation with lottie.
 If there is an error, from the connexion or not, a popup is shown with the error message.
 
 #### The [ERC 20 tokens Activity](https://github.com/skategui/argent-tech-test/tree/master/app/src/main/java/agis/guillaume/argent/ui/list)
 
 Display the list of tokens owned by the current account, with for each, the balance  IN ETH and the number of tokens owned.
 It displayed a "warning" icon instea of the balance if there was an issue loading the market rate for that pair (X_ETH).
 If the list is empty, a lottie animation is displayed.
 

### Project

- config folder contains everything related to the config of the app, from the variables used in debug and prod to the group of gradle dependencies.
- app/src/androidTest contains the espresso tests (**6 espresso tests**) 
- app/src/sharedTest contains the classes shared and used in unit and espresso tests
- app/src/test contains the unit tests. (**31 unit tests**) with [Truth](https://github.com/google/truth)

### Questions


#### How long did you spend on the exercise?

around 8hours. It makes me longer to write some java than Kotlin.
Also wasted some time to make sure the precision was correct and verify the data displayed.

#### What would you improve if you had more time?

- Obviously switch to kotlin to remove the awful java boilerplate (then let the developers more focus on his job, the logic) and use immutable objects (already in kotlin).

- Have a project in multi modules in mono repo, to split up the team work and make the build faster (as it will only recompile the module modified and not the whole project), where each module could be a big feature.
Also, each module will be able to use the config, then use the same version of each dependencies.

- Network : Cache the requests **smartly** with a different expire time given the resource to access.
- Network : Add some paging on the resources that intent to return a big list of data. To avoid the user to wait too long and **use less memory/less battery**.
- Network : **Take in consideration the connexion speed** to load a different amount of data per request, and make sure the user does not wait too long to get the response of the request.
- Network :  use https://developers.google.com/protocol-buffers/ or https://google.github.io/flatbuffers/ to **transit smaller on-the-wire packet size, then make the requests faster**.
Benchmark of the performance : https://codeburst.io/json-vs-protocol-buffers-vs-flatbuffers-a4247f8bda6f
- Offline :  Usage of **Room** to store some data and have some consistency between sessions, also in order to preload some views and be able to use the app offline. (better UX) 

- Network / Coroutine : Use coroutine instead of RxAndroid for the request calls. Coroutine is lighter, faster and consume less memory. Better use RxAndroid when we wish to have a reactive app (with Flowable for instance.) [benchmark available here](https://proandroiddev.com/kotlin-coroutines-vs-rxjava-an-initial-performance-test-68160cfc6723)


- App : Create a light version / less greedy of the app for country that does not have high quality android devices (such as africa or asia?)

- CI : Add a CI to build + run the unit tests after each commit / add a nightly build that will run all the tests (junit + espresso, as it's longer to run).

- Add more tests. I could add more espresso tests.


#### What would you like to highlight in the code?

1. The architecture of the app using Dagger2, Retrofit, RxAndroid, MVI, Clean architecture, SOLID Principes.
2. The usage of BigDecimal to avoid loosing in precision. It's really important, mainly in fintech.
3 . The improvements already made :

- Use annotation @Binds instead of @Provides when possible to have less generated files from Dagger2.
- Does not reload users already fetched from the server. Currently store in memory but could use room if we wish to have some data consistency and be able to use the app offline.
- Network : Cache the requests for 10 minutes in order to not remake the request to the server, then consume less battery and get the data faster.



#### If you had to store the private key associated to this ETH account address, how would you make that storage secure ? 

I would use the [Android keystore](https://developer.android.com/training/articles/keystore). It's made to store public / private keys with all the security necessary.



By Guillaume Agis 

With love and passion <3

