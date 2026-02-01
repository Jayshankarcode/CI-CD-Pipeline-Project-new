End-to-End CI/CD & GitOps Pipeline using Jenkins, Docker, Kubernetes & Argo CD
<img width="940" height="645" alt="image" src="https://github.com/user-attachments/assets/40592b1d-e2c6-4899-9976-fc281b2397f4" />

🔹 Project Overview 
I implemented an end-to-end CI/CD pipeline with GitOps principles for a Spring Boot application.
The solution automates build, containerization, code quality checks, image publishing, and Kubernetes deployment using Jenkins and Argo CD.________________________________________
🔹 Architecture & Tools Used
🔧 Technology Stack
Application: Spring Boot
Build Tool: Maven
CI Tool: Jenkins (Dockerized)
Code Quality: SonarQube
Containerization: Docker
Container Registry: Docker Hub
Orchestration: Kubernetes (Minikube)
CD / GitOps: Argo CD
Version Control: GitHub
________________________________________
High-Level Architecture Flow
Developer → GitHub → Jenkins → Docker Hub → GitHub (manifests) → Argo CD → Kubernetes

CI Pipeline (Jenkins) – VERY IMPORTANT SECTION
🔹 What Jenkins does in this project
Jenkins is responsible only for Continuous Integration, not deployment.
🔹 Jenkins Pipeline Stages
✅ Stage 1: Source Code Checkout
Jenkins pulls application code from GitHub
Ensures pipeline always runs on the latest commit
✅ Stage 2: Build & Test
Maven compiles the Spring Boot application
Runs unit tests
Generates JAR artifact
✅ Stage 3: Static Code Analysis (SonarQube)
Jenkins sends code to SonarQube
Checks for:
Bugs
Code smells
Security issues
Fails the build if quality gate fails
✅ Stage 4: Docker Image Build & Push
Jenkins builds a Docker image using Java 17
Image is tagged with Jenkins build number (example: springboot-cicd:13)
Image is pushed to Docker Hub
✅ Stage 5: Update Kubernetes Manifest in Git
Jenkins updates only the image tag in deployment.yml
Commits and pushes this change back to GitHub

Why GitOps?
Instead of Jenkins deploying to Kubernetes, I followed GitOps principles where Git is the single source of truth. Argo CD continuously monitors Git and reconciles the cluster state automatically.

CD Pipeline (Argo CD) – CORE CONCEPT
🔹 What Argo CD does
Watches a specific folder in GitHub:
spring-boot-app-manifests/
Detects any change in Kubernetes YAML
Automatically applies changes to Kubernetes
🔹 Argo CD Application Configuration
Repository: GitHub
Branch: main
Path: Kubernetes manifests
Sync Policy:
Automated
Self-heal enabled
Prune enabled
📌 Key:
Argo CD ensures the Kubernetes cluster always matches what is defined in Git.
________________________________________
7️.Kubernetes Deployment
🔹 Kubernetes Objects Used
Deployment
Manages replicas
Handles rolling updates
Service (NodePort)
Exposes application to external users
🔹 Deployment Flow
Argo CD applies manifests
Kubernetes pulls Docker image
Pods are created automatically
Service exposes the application
Proof of Automatic Deployment
To verify automatic deployment, I changed only the image tag in Git. Argo CD detected the change, marked the app OutOfSync, then automatically synced it, restarted pods, and deployed the new version without any kubectl command.
•  Argo CD UI shows Synced & Healthy
•  Pod age changes after deployment
Real Problems You Faced (VERY IMPORTANT)
Interviewers LOVE this section.
🔴 Problem 1: ImagePullBackOff
Cause: Image tag updated in manifest but image not built
Fix: Ensured Jenkins builds & pushes image before updating manifest
🔴 Problem 2: Jenkins executor blocked
Cause: Low disk space on Jenkins node
Fix: Cleaned Docker images and workspaces
🔴 Problem 3: Workspace permission issue
Cause: Docker ran as root, Jenkins couldn’t delete files
Fix: Corrected ownership and standardized user execution
These issues helped me understand real production-level CI/CD challenges.
________________________________________
🔟 Why This Project Is Production-Ready
CI and CD are fully separated
No manual deployments
Git is the single source of truth
Self-healing enabled
Easy rollback via Git
Jenkins is disposable



## Installation on EC2 Instance




Install Jenkins, configure Docker as agent, set up cicd, deploy applications to k8s and much more.

## AWS EC2 Instance

- Go to AWS Console
- Instances(running)
- Launch instances
We are going to setup Ec2 instance
<img width="940" height="395" alt="image" src="https://github.com/user-attachments/assets/b824eba3-31ac-4ad2-8fc3-d868949d3978" />
<img width="940" height="411" alt="image" src="https://github.com/user-attachments/assets/4e7e281b-ac5c-4ebc-b93f-75679fdf9519" />
I have selected T2large Instance type.
<img width="940" height="258" alt="image" src="https://github.com/user-attachments/assets/5c78d888-f833-4fcb-8cc6-e55ad23611d7" />
We have created a Keyvalue pair.
<img width="903" height="786" alt="image" src="https://github.com/user-attachments/assets/f8cc23e8-fb9f-4448-94d8-ac185b11248b" />
Finally Ec2 instance is created.

<img width="940" height="146" alt="image" src="https://github.com/user-attachments/assets/06f4171b-3f3a-4908-a4fc-74b5416351e3" />
Now ssh into the instance.
<img width="916" height="794" alt="image" src="https://github.com/user-attachments/assets/8400d700-bcc4-4046-8755-8d4b13737fcb" />

### Install Jenkins.

Pre-Requisites:
 - Java (JDK)

### Run the below commands to install Java and Jenkins

Install Java


sudo apt update

<img width="940" height="830" alt="image" src="https://github.com/user-attachments/assets/0e77e312-4528-4b5d-942b-bd723476d80b" />


sudo apt install openjdk-17-jre
<img width="1353" height="555" alt="image" src="https://github.com/user-attachments/assets/5d743129-76cc-4cd4-bc7b-43f2247457c5" />



Verify Java is Installed


java -version
<img width="570" height="64" alt="image" src="https://github.com/user-attachments/assets/5a821f92-8095-4aff-b5c1-75bda9c8c210" />

```

Now, you can proceed with installing Jenkins

```
We have follow below steps to install jenkins.
As discussed, on Ubuntu 24.04, the correct professional approach is:
✅ Run Jenkins using Docker
This avoids:
GPG key mess
OS repo issues
Version conflicts
And it is 100% acceptable for interviews & real projects.
🔥 DO THIS NOW — Docker-based Jenkins (Step-by-step)

1️.Install Docker
sudo apt update -y
sudo apt install -y docker.io
Start & enable Docker:
sudo systemctl start docker
sudo systemctl enable docker
Verify Docker:
docker --version

2️.Allow ubuntu user to run Docker
sudo usermod -aG docker ubuntu
⚠️ Logout and login again (very important):
exit
Then SSH again:
ssh -i /c/Users/Dell/Downloads/Jay-CICD-Project.pem ubuntu@34.227.65.30


3️.Run Jenkins container
docker run -d \
  --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts
Check container:
docker ps
You should see jenkins running.
4.Get Jenkins admin password
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword


5️.Open Jenkins in browser
From your laptop:
http://34.227.65.30:8080
🎉 Jenkins UI will load.

<img width="1323" height="641" alt="image" src="https://github.com/user-attachments/assets/baa16951-1352-4bb6-8b8a-b8674d67d138" />

<img width="1305" height="689" alt="image" src="https://github.com/user-attachments/assets/f38f62b3-1f21-47ef-a116-7235981e7ac6" />



**Note: ** By default, Jenkins will not be accessible to the external world due to the inbound traffic restriction by AWS. Open port 8080 in the inbound traffic rules as show below.

- EC2 > Instances > Click on <Instance-ID>
- In the bottom tabs -> Click on Security
- Security groups
- Add inbound traffic rules as shown in the image (you can just allow TCP 8080 as well, in my case, I allowed `All traffic`).

- <img width="1355" height="519" alt="image" src="https://github.com/user-attachments/assets/087c3343-7ff8-4dfc-b657-a57e7aab49bc" />

- <img width="1075" height="194" alt="image" src="https://github.com/user-attachments/assets/620e07db-49e4-4b48-b842-bb74e7d5fc68" />

### Login to Jenkins using the below URL:

http://34.227.65.30:8080   [You can get the ec2-instance-public-ip-address from your AWS EC2 console page]

Note: If you are not interested in allowing `All Traffic` to your EC2 instance
      1. Delete the inbound traffic rule for your instance
      2. Edit the inbound traffic rule to only allow custom TCP port `8080`
  
After you login to Jenkins, 
      - Run the command to copy the Jenkins Admin Password - docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
`
      - Enter the Administrator password
      
<img width="1239" height="684" alt="image" src="https://github.com/user-attachments/assets/9f75d4d5-0497-4ceb-abfe-3e5458bf61fc" />


### Click on Install suggested plugins

<img width="1291" alt="Screenshot 2023-02-01 at 10 58 40 AM" src="https://user-images.githubusercontent.com/43399466/215959294-047eadef-7e64-4795-bd3b-b1efb0375988.png">

Wait for the Jenkins to Install suggested plugins

<img width="1291" alt="Screenshot 2023-02-01 at 10 59 31 AM" src="https://user-images.githubusercontent.com/43399466/215959398-344b5721-28ec-47a5-8908-b698e435608d.png">

Create First Admin User or Skip the step [If you want to use this Jenkins instance for future use-cases as well, better to create admin user]

<img width="990" alt="Screenshot 2023-02-01 at 11 02 09 AM" src="https://user-images.githubusercontent.com/43399466/215959757-403246c8-e739-4103-9265-6bdab418013e.png">

Jenkins Installation is Successful. You can now starting using the Jenkins 

<img width="1035" height="672" alt="image" src="https://github.com/user-attachments/assets/404de1e1-88ca-4d82-a1ec-3ff55fc535ce" />

<img width="1366" height="617" alt="image" src="https://github.com/user-attachments/assets/f06bf383-70e7-4363-b34e-59988a0cf8f9" />


## Install the Docker Pipeline plugin in Jenkins:

   - Log in to Jenkins.
   - Go to Manage Jenkins > Manage Plugins.
   - In the Available tab, search for "Docker Pipeline".
   - Select the plugin and click the Install button.
   - Restart Jenkins after the plugin is installed.

   - ✅ Install Docker Pipeline Plugin in Jenkins
🔹 Step 1: Go to Jenkins Dashboard

From Jenkins home:

Manage Jenkins → Plugins

🔹 Step 2: Install plugin

Click Available plugins

In the search box, type:

Docker Pipeline


Select:
✅ Docker Pipeline (by CloudBees)

🔹 Step 3: Install

Click Install without restart
(recommended)

Wait until installation completes.

🔹 Step 4: Restart Jenkins (recommended)

Even if Jenkins doesn’t force it, do a safe restart:

From browser:

http://34.227.65.30:8080/restart


Or via terminal:

docker restart jenkins

✅ Verify Plugin Installed

Go to:

Manage Jenkins → Plugins → Installed


Search for:

Docker Pipeline


If you see it → ✔️ done.


   
<img width="1392" alt="Screenshot 2023-02-01 at 12 17 02 PM" src="https://user-images.githubusercontent.com/43399466/215973898-7c366525-15db-4876-bd71-49522ecb267d.png">

<img width="1226" height="683" alt="image" src="https://github.com/user-attachments/assets/790c09d0-7c0b-4908-a831-e0d3d10301a0" />

Wait for the Jenkins to be restarted.


Now we will create a pipeline in jenkins.

<img width="1241" height="632" alt="image" src="https://github.com/user-attachments/assets/c8aa154d-98d6-4599-be68-7b925be86185" />

<img width="1275" height="578" alt="image" src="https://github.com/user-attachments/assets/197ed80a-40f8-47b3-bf9a-64fad6acb3d0" />



## Docker Slave Configuration

Run the below command to Install Docker

```
sudo apt update
sudo apt install docker.io
```
 
### Grant Jenkins user and Ubuntu user permission to docker deamon.

```
sudo su - 
usermod -aG docker jenkins
usermod -aG docker ubuntu
systemctl restart docker
```

Once you are done with the above steps, it is better to restart Jenkins.

```
http://<ec2-instance-public-ip>:8080/restart
```

<img width="1352" height="118" alt="image" src="https://github.com/user-attachments/assets/aaabc0c3-e179-412d-bf8f-8fa067c70d40" />


The docker agent configuration is now successful.

Installation of miniKube: Minikube Installation (First Time – Docker Driver)

✅ Prerequisites
1️ OS
•	Linux / macOS / Windows
(You are on Windows + WSL / Git Bash, so this works)
2️ Docker (Mandatory)
Verify Docker is installed & running:
docker version
 Step 1: Install kubectl
Linux / WSL
curl -LO https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl
chmod +x kubectl
sudo mv kubectl /usr/local/bin/

Verify:
kubectl version --client
________________________________________
Windows (PowerShell – Admin)
choco install kubernetes-cli -y
Verify:
kubectl version --client
________________________________________
🧩 Step 2: Install Minikube
Linux / WSL
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
Verify:
minikube version
________________________________________
Windows
choco install minikube -y
Verify:
minikube version
________________________________________
 Step 3: Start Minikube (IMPORTANT)
⭐ Use Docker driver (BEST for beginners)
minikube start --driver=docker
⏳ First time takes 2–5 minutes
Expected output:
Done! kubectl is now configured to use "minikube"
________________________________________
 Step 4: Verify Kubernetes Cluster
kubectl get nodes
Expected:
minikube   Ready
Check pods:
kubectl get pods -A
________________________________________
🌐 Step 5: Access Kubernetes Dashboard (Optional)
minikube dashboard
Browser opens automatically.

##Now we will install ArgoCD by using operator.
Search: https://operatorhub.io/
<img width="1252" height="348" alt="image" src="https://github.com/user-attachments/assets/27262d22-b60a-4718-9d04-afe93f02b955" />
Search for ArgoCD.
<img width="1285" height="682" alt="image" src="https://github.com/user-attachments/assets/933be9d8-821a-4ddf-b562-f09a4e1e06fd" />
Now instal.
<img width="1159" height="680" alt="image" src="https://github.com/user-attachments/assets/5d0251df-1f0d-44e3-8480-6f1ae72143f2" />

<img width="1346" height="491" alt="image" src="https://github.com/user-attachments/assets/15ddfb51-d3c8-4737-be9f-67f0ad6a85f5" />

<img width="1089" height="155" alt="image" src="https://github.com/user-attachments/assets/afd0a9b6-1923-409b-bf27-93300abe0d3a" />



I was faing error so ✅ Install ArgoCD WITHOUT Operator (Industry Standard for Local)

This is what 99% DevOps engineers do locally.

🧩 Step 1: Clean up broken OLM (safe)
kubectl delete namespace olm operators

🧩 Step 2: Create ArgoCD namespace
kubectl create namespace argocd

🧩 Step 3: Install ArgoCD (official way)
kubectl apply -n argocd \
-f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

⏳ Wait 1–2 minutes

<img width="679" height="687" alt="image" src="https://github.com/user-attachments/assets/c574995f-f57b-4394-badb-22aa6208f1d9" />


🧩 Step 4: Verify pods
kubectl get pods -n argocd

<img width="639" height="159" alt="image" src="https://github.com/user-attachments/assets/444dd431-0c94-44d1-8949-dcf58503916e" />


All should be Running

🧩 Step 5: Expose ArgoCD UI (Minikube)
kubectl patch svc argocd-server -n argocd \
-p '{"spec": {"type": "NodePort"}}'

<img width="352" height="65" alt="image" src="https://github.com/user-attachments/assets/23884257-caa4-4a95-b14a-5e6ce17badd6" />

Then:

minikube service argocd-server -n argocd

<img width="816" height="263" alt="image" src="https://github.com/user-attachments/assets/4c62842f-150c-482c-89ac-ae9a495135dc" />

Browser opens 🎉

<img width="1315" height="615" alt="image" src="https://github.com/user-attachments/assets/00dfe736-ea3f-4697-b902-bffc4f6546f8" />


🔐 Step 6: Login credentials

Username

admin


Password

kubectl get secret argocd-initial-admin-secret -n argocd \
-o jsonpath="{.data.password}" | base64 --decode

<img width="1326" height="570" alt="image" src="https://github.com/user-attachments/assets/f1ab162e-0b52-4dff-9d76-b10d9ae24f15" />


Now we will create the docker crendiential in Jenkins.
<img width="1247" height="667" alt="image" src="https://github.com/user-attachments/assets/1a1de1a5-f808-4a88-a302-b585a54a4092" />
<img width="1329" height="518" alt="image" src="https://github.com/user-attachments/assets/a381a966-baab-4509-96bc-7281b4f67ec3" />

Now we will create Github crediential in Jinkins.

We need to create a token in github.

<img width="1261" height="622" alt="image" src="https://github.com/user-attachments/assets/7bde748e-e2b6-43e7-a8e1-cd47d294df56" />
<img width="1337" height="626" alt="image" src="https://github.com/user-attachments/assets/27f46d38-aaf4-4ece-83f5-afc7117001f0" />
Now copy the token and paste in secret.
<img width="1272" height="665" alt="image" src="https://github.com/user-attachments/assets/720c1d4f-0510-4d34-85c4-8a9bef900d31" />

<img width="1231" height="422" alt="image" src="https://github.com/user-attachments/assets/f0025f41-a362-4afe-b7dc-2806f333b85a" />

Now we will build the pipeline from jenkins and will fix if any error.
<img width="1351" height="470" alt="image" src="https://github.com/user-attachments/assets/95296db7-cadf-4f4d-9cbd-5ff08270f9c7" />

<img width="1332" height="335" alt="image" src="https://github.com/user-attachments/assets/611056b5-9faf-441f-ae61-6773fc569974" />

<img width="1335" height="376" alt="image" src="https://github.com/user-attachments/assets/a235055c-e483-4977-b6ea-e8c89fa91b92" />
<img width="1292" height="616" alt="image" src="https://github.com/user-attachments/assets/f9734692-e0a6-4d39-8b48-b6f906b13057" />

So we have completed the CI part.
--------------------------------------------------------------------------

Now we will see the CD part where Argocd will take care of deployment.
We will check the kubernetes cluster status.
Minikube status.

<img width="737" height="165" alt="image" src="https://github.com/user-attachments/assets/d42f5f87-3aae-4426-9a60-4545b96b70f4" />
create ONE Argo CD Application that points to the repo
Create Argo CD Application YAML (GitOps way)

Create a file springboot-argocd-app.yml (name can be anything):
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: springboot-app
  namespace: argocd
spec:
  project: default

  source:
    repoURL: https://github.com/Jayshankarcode/CI-CD-Pipeline-Project-new.git
    targetRevision: main
    path: java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests

  destination:
    server: https://kubernetes.default.svc
    namespace: default

  syncPolicy:
    automated:
      prune: true
      selfHeal: true
Apply it:
kubectl apply -f springboot-argocd-app.yml

OPTION 2: Create Application via Argo CD UI (simpler)
This is also 100% valid (many teams do this).
Fill like this:
•	Application name: springboot-app
•	Repo URL:
https://github.com/Jayshankarcode/CI-CD-Pipeline-Project-new.git
•	Path:
java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests
•	Revision: main
•	Cluster: https://kubernetes.default.svc
•	Namespace: default
Click Create → Sync.

<img width="575" height="40" alt="image" src="https://github.com/user-attachments/assets/59d28174-6f82-4172-996b-12f83456edc6" />

Check Argo CD UI

Open Argo CD UI → you should see:

springboot-app

Status: Synced

Health: Healthy (or progressing → healthy)

<img width="1237" height="663" alt="image" src="https://github.com/user-attachments/assets/7a51174e-cae6-4733-9d03-51f8b8061980" />

2.check Kubernetes resources
kubectl get pods
kubectl get deploy
kubectl get svc
You should see Spring Boot pods starting
<img width="600" height="70" alt="image" src="https://github.com/user-attachments/assets/99249357-0230-46b5-a81d-df08b0ccc3c4" />

<img width="940" height="94" alt="image" src="https://github.com/user-attachments/assets/c059454b-a9b4-47f5-852c-fc5c321a1204" />

Finally Argo CD deployed automatically (GitOps proof)

<img width="940" height="431" alt="image" src="https://github.com/user-attachments/assets/5c7eab8a-3ec5-4106-b696-a87e01f49a64" />
<img width="1231" height="659" alt="image" src="https://github.com/user-attachments/assets/991d8818-9529-4dae-bb9f-0c8794f15cdb" />

Now the ONLY remaining step: View the application

Find the service name

Run:

kubectl get svc


You should see something like:

spring-boot-app   NodePort   ...
<img width="664" height="75" alt="image" src="https://github.com/user-attachments/assets/12b027cc-bf6c-49db-805b-9f071732542e" />


2️⃣ Open the app (Minikube way)

Run:

minikube service spring-boot-app

<img width="940" height="211" alt="image" src="https://github.com/user-attachments/assets/1801b305-3610-4822-9a6f-0fc60e39d995" />


This will:

Open browser automatically
OR

Give you a URL → open it manually


<img width="1249" height="643" alt="image" src="https://github.com/user-attachments/assets/019e64a7-28a0-45e3-9876-f97f151f7dd3" />
