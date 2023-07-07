The order of services to be deployed:
1. Discovery Service (SERVICE_DIR = ROOT_DIR/discovery/server)
2. Authentication Service (SERVICE_DIR = ROOT_DIR/auth)
3. Email Communication Service (SERVICE_DIR = ROOT_DIR/communications/email)
4. SMS Communication Service (SERVICE_DIR = ROOT_DIR/communications/sms)
5. Delivery Service  (SERVICE_DIR = ROOT_DIR/delivery)
6. Card Payments Service (SERVICE_DIR = ROOT_DIR/payments/card)
7. Mobile Payments Service (SERVICE_DIR = ROOT_DIR/payments/mobile)
8. Sales Items Service (SERVICE_DIR = ROOT_DIR/sales/items)
9. Sales Orders Service (SERVICE_DIR = ROOT_DIR/sales/orders)

WSO2 MI Integrated API can be found at SERVICE_DIR = ROOT_DIR/mi

ROOT_DIR is the directory of the folder where the project is cloned.

Pre-requisites:

1. Install PostgreSQL server on the local machine:
   - MacOS: [https://postgresapp.com](https://postgresapp.com)
   - Windows: [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)

2. Clone the repository if you do not have the source code with you at the moment.
   - Repository URL: [http://github.com/chandeepadissanayake/shopping-base](http://github.com/chandeepadissanayake/shopping-base)

3. WSO2 Micro Integrator Studio
   - All Platforms: [https://wso2.com/micro-integrator/](https://wso2.com/micro-integrator/)

Steps for Deployment:

1. Modify the contents of the `.env` file at `ROOT_DIR/.env` to suit your configuration of the database. The fields in it are self-explanatory.
2. For each service above, pull up a terminal and navigate to the `SERVICE_DIR` of the respective service.
3. Run the following command to run each service after replacing the port:
   - Command: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DSERVER_PORT={REPLACE_SERVER_PORT}"`
   - Replace the placeholder `REPLACE_SERVER_PORT` in the above command with any unused TCP port on your machine. Make sure you replace the placeholder along with the braces.
   - **IMPORTANT**: `REPLACE_SERVER_PORT` for the Discovery Service must strictly be `8090`. For other services, it's your call.

Steps for Deployment of the Integrated API:

1. Open the `mi` folder in WSO2 MI studio.
2. Right-click "shopping-base-doCompositeExporter" and click on "Export Project Artifacts and Run".
3. Look up for the port at which the service is running.

**IMPORTANT Note**: In order to run the integrated API, you do not need to deploy the aforementioned services, as integration has been done for the cloud-deployed services.

**Note**: If you can get the Integrated API built from the command line with Maven, the WSO2 MI studio is not required.
