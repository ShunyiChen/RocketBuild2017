# The REST API for Integrating TFE and TRUcore
TFE integration[Done]
The project is to replace the current, single-threaded, SOAP +FTP       implementation with a multi-threaded, TPA implementation.
Expanding TFE integration[In progress]
We will build a RESTful Web Service to expand the TFE integration with the TPA SDK and microservices using Spring,Spring Boot and Spring Cloud, so that to increase the services for mobile phone, tablet or some other existing systems to access. Take mobile phone for example, using the phone, the TFE user can send files to or receive files from internal users, and send files to external partners such as suppliers.
![image](https://github.com/ShunyiChen/Build2017/blob/master/OverallLayout.png)
![image](https://github.com/ShunyiChen/Build2017/blob/master/MicroservicesWithSpring.png)
API Gateway
An API Gateway is a single point of entry (and control) for front end clients, which could be browser based or mobile. The client only has to know the URL of one server, and the backend can be refactored at will with no change, which is a significant advantage. There are other advantages in terms of centralization and control: rate limiting, authentication, auditing and logging.
![image](https://github.com/ShunyiChen/Build2017/blob/master/DeployMicroservicesInTheHybridCloud.png)
API Details
TFE user login – The user account is only authenticated with TRUfustion and generate and save the session id on the server.
Get available transactions – Get available transactions by calling the TPA SDK.
Get contact list or partners – Get the contact list or partner list by reading from the TFE relational database table in business logic classes.
Sending files – Send file from mobile phone to TRUcore server by calling the TPA SDK to encrypt the file and upload it over the UDT protocol.
Receiving files – Write a business class for polling and checking whether there are new transactions in the list. If so, download the file and decrypt it by calling the TPA SDK over the UDT protocol.
TFE user logout – The same as the logic in TRUFusion.

##How to run the services.

1.Commands for building project:<br>
cd \rocket-build2017\RB2017<br>
mvn clean intall<br>

2.Commands for starting up services:<br>
cd \rocket-build2017\RB2017\discovery<br>
java -jar target/discovery-1.0.jar<br>
cd \rocket-build2017\RB2017\auth<br>
java -jar target/auth-1.0.jar<br>

3.Test in JMeter<br>
Open the JMeter<br>
Import the Build.2017.jmx<br>
run the rest-login/reset-hello<br>



