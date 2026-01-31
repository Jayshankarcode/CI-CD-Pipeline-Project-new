# Spring Boot based Java web application
 
This is a simple Sprint Boot based Java application that can be built using Maven. Sprint Boot dependencies are handled using the pom.xml 
at the root directory of the repository.

This is a MVC architecture based application where controller returns a page with title and message attributes to the view.

## Execute the application locally and access it using your browser

Checkout the repo and move to the directory

```
git clone https://github.com/iam-veeramalla/Jenkins-Zero-To-Hero/java-maven-sonar-argocd-helm-k8s/sprint-boot-app
cd java-maven-sonar-argocd-helm-k8s/sprint-boot-app
```

Execute the Maven targets to generate the artifacts

```
mvn clean package
```

The above maven target stroes the artifacts to the `target` directory. You can either execute the artifact on your local machine
(or) run it as a Docker container.

** Note: To avoid issues with local setup, Java versions and other dependencies, I would recommend the docker way. **


### Execute locally (Java 11 needed) and access the application on http://localhost:8080

```
java -jar target/spring-boot-web.jar
```

### The Docker way

Build the Docker Image

```
docker build -t ultimate-cicd-pipeline:v1 .
```

```
docker run -d -p 8010:8080 -t ultimate-cicd-pipeline:v1
```

Hurray !! Access the application on `http://<ip-address>:8010`


## Next Steps

### Configure a Sonar Server locally

```
System Requirements
Java 17+ (Oracle JDK, OpenJDK, or AdoptOpenJDK)
Hardware Recommendations:
   Minimum 2 GB RAM
   2 CPU cores
sudo apt update && sudo apt install unzip -y
adduser sonarqube
wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-10.4.1.88267.zip
unzip *
chown -R sonarqube:sonarqube /opt/sonarqube
chmod -R 775 /opt/sonarqube
cd /opt/sonarqube/bin/linux-x86-64
./sonar.sh start
```

SonarQube using Docker ✅
Step 1: Run SonarQube container
docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  sonarqube:10.4-community

  <img width="574" height="241" alt="image" src="https://github.com/user-attachments/assets/b019ecc5-1e69-4f93-a2dc-bc7b17921139" />



Wait ~2 minutes.

Step 2: Open SonarQube UI
http://34.227.65.30:9000


Login:

username: admin
password: admin

<img width="1227" height="605" alt="image" src="https://github.com/user-attachments/assets/8021b5f8-07ac-4492-9e92-36860c58b480" />


It will force password change.

<img width="1326" height="579" alt="image" src="https://github.com/user-attachments/assets/f1765d7c-cf7b-42c3-bdeb-211b79e5aa5c" />


Step 3: Generate Sonar token
Administration → Security → Users → Tokens

<img width="1339" height="573" alt="image" src="https://github.com/user-attachments/assets/6318a446-2c21-4033-b502-9a2b2616fb00" />

<img width="1034" height="437" alt="image" src="https://github.com/user-attachments/assets/0b381945-1ca8-4dd6-a7a2-db081b48f2a7" />

✅ Create the SonarQube Token (do this now)
On this screen:
🔹 Step 1: Fill the details
•	Name: jenkins-token
•	Expires in:
o	You can keep 30 days (fine for learning)
🔹 Step 2: Click Generate
•	SonarQube will show you a long token string

Create token → copy it.

Step 4: Add token to Jenkins
Manage Jenkins → Credentials → Global → Add Credentials

<img width="1350" height="676" alt="image" src="https://github.com/user-attachments/assets/11f29bc9-2a02-4683-95b1-8ea9057eeb69" />

<img width="1257" height="662" alt="image" src="https://github.com/user-attachments/assets/f6b83840-68c9-4220-8077-849579b24d36" />

✅ What you MUST select (correct way)
🔹 Step 1: Change Kind

Click the Kind dropdown and select:

✅ Secret text

🔹 Step 2: Fill fields like this

After selecting Secret text, fill:

Scope:
✅ Global (leave as is)

Secret:
👉 Paste the SonarQube token you generated

ID:

sonarqube


⚠️ Must be exactly sonarqube

Description:

SonarQube token


Then click Create.

<img width="1245" height="307" alt="image" src="https://github.com/user-attachments/assets/50ae0c69-c1d9-4427-94af-428fd1b1a36a" />



Hurray !! Now you can access the `SonarQube Server` on `http://<ip-address>:9000` 


