IDE Setup
============

First, download and install openJDK8, this step varies according to your operating system. 
For Windows: http://jdk.java.net/8/
For Linux: http://openjdk.java.net/install/
Then, download, extract and run Eclipse Mars Eclipse IDE for Java Developers https://www.eclipse.org/mars/ 
Choose any folder as your workspace.

 ![figure 1](resources/1528728397300.png)



Make sure the jdk1.8.0 is selected. If it does not show up, you can use the search option and point to the Java installation path. (On Windows this is usually under C:\Program Files\Java)

 ![figure 2](resources/1528728447646.png)



 ![figure 3](resources/1528728474347.png) 



In order to setup our development environment, we need to install a plugin called **Remote System Explorer**. This plugin can be installed via the **Install New Software** option of the **Help** menu.

 ![figure 4](resources/clip_image001.png)



  ![figure 5](resources/clip_image003.png)



Next, we need to download the JSCH plugin (jsch-xx.jar) and place it in the following directory:

eclipse\plugins\org.apache.ant_1.9.6.v201510161327\lib

<https://sourceforge.net/projects/jsch/>

This plugin will be used to deploy the Java bytecode to the target.

 ![figure 6](resources/clip_image005.png)



 ![figure 7](resources/clip_image007.png)



We can now start to setup our project. First, let’s import a Maven project via **File->New->Other->Maven Project** option. Create a simple project (skip archetype selection). Follow the setup as indicated in the figure 14 to 16.

 ![figure 8](resources/clip_image008.png)

 

 ![figure 9](resources/clip_image010.png)



 ![figure 10](resources/clip_image012.png)



At this point you need to copy the provided **pom.xlm remoteDebug.xml** and **all .java** files to the workspace as shown in the figure 17. 

Switch to the Java perspective

 ![figure 11](resources/clip_image013.png)



Also, make sure the JRE System Library is jdk1.8.0. If this is not the case you can set it by doing right click on the project->Properties->Workspace default JRE (jdk1.8.0) Press F5 to refresh the project if the files do not show up. 
An important step is also to configure Eclipse to use the java language compliance level of 1.6 to support interfaces overriding.

You might have to manually create the “skyhawk” directory under src/main/java

 ![figure 12](resources/clip_image014.png)



We can now package everything. Run Maven Install to download the project dependencies and to package the example into a .jar

  ![figure 13](resources/clip_image016.png)



**For remote debugging only :**

We are almost ready to test the program on the target. First, we need to tell eclipse to use the remote debug Ant task. Double click on **Ant Build** to create the remote debug task.

 ![figure 14](resources/clip_image017.png)



  ![figure 15](resources/clip_image019.png)



Run the remote deploy and debug ant task.
 This will install the application on the target and execute it in remote debugging mode.

 ![figure 16](resources/clip_image020.png)



If successful, the console should show the following results.

 ![figure 17](resources/clip_image022.png)

 

Now all that is left to do is start the debugger. Make sure you specify the IP address of the device and the port 4000.

 ![figure 18](resources/clip_image023.png)



 ![figure 19](resources/clip_image025.png)



We can also use the remote system explorer to send commands to the target or visualize processes and the filesystem.

 ![figure 20](resources/clip_image026.png)

 

 ![figure 21](resources/clip_image027.png)



To do so, we need to setup a connection to the device. Choose **SSH Only**.

 ![figure 22](resources/clip_image029.png)



 ![figure 23](resources/clip_image031.png)



At this point you can launch a Shell that will be connected to the target via SSH.

 ![figure 24](resources/clip_image032.png)



Enter the credentials
 user: **root** 
 password: **temp**

 ![figure 25](resources/clip_image033.png)



Here is a view of the target filesystem and as we can see the .jar has been deployed to the **/opt/skyhawk** folder.

 ![figure 26](resources/clip_image034.png)
