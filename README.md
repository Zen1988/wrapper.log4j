# wrapper.log4j
Log4j 1.x being announced as EOL since August 2015, and yet, there are a lot of enterprise grade solution still make use of log4j 1.x. Due to recent log4j 2.x zero day 
vulnerabilities, all log4j related question being surface one by one, included used of EOL version of library. While log4j 1.x does not affected by log4j 2.x vulnerabilities, it 
does have it's own vulnerabilities/issue. As the person stuck between operation, with product support, I personally feel frustrated. 

Below are solution you can migrate from log4j 1.x, log4j 2.x:
1)	Upgrade from 2.x to 2.17.0/or higher
<ol>
<li> Scan for file with filename contains/start with log4j. The result should look like log4j-core-2.13.3.jar log4j-api-2.13.3.jar. </li>
<li> Backup all of the jar files either zipped it/tar it, in case any unexpected failure occur or we have to upgrade the products. </li>
<li> Copy log4j binary with similar naming convention, e.g. log4j-core-2.17.1.jar, log4j-api-2.17.1.jar into the same directory. </li>
<li> Change the file ownership and permission to what original log4j file have. </li>
<li> Scan for any web.xml, MANIFEST.MF under same folder structure (can be same folder/or one level above). Confirm no hardcoded class path point to deleted library, else we will have to update the reference.</li>
<li> Go to product binary folder, for example bin/bin64. Check for the script used to initialize the application, e.g. startup.sh, confirm no hardcoded classpath point to deleted library, else we will have to update the reference.</li>
</ol>
2)	Upgrade from 1.x to 2.17.0/or higher
<ol>
<li> Scan for file with filename contains/start with log4j. The result should look like log4j.jar log4j-1.2.17.jar. Compare to 2.x, which have clear split of functionality per jar, log4j 1.x is one big binary contains most of the function. </li>
<li> Backup all of the jar files either zipped it/tar it, in case any unexpected failure occur or we have to upgrade the products. </li>
<li> Unlike log4j 2.x, we are not clear which function, so, we need to copy all 3 basic for log4j-1.x to log4j-2.x migration: log4j-1.2-api-2.17.1.jar (This is bridge api allow old application call on old log4j package path), log4j-api-2.17.1.jar, log4j-core-2.17.1.jar. </li>
<li> After that, we have to list the current directory, to guessed which additional jar is require. E.g. if we saw common-logging-x.jar, then we will need to copy log4j-jcl-
2.17.1.jar; and if we saw slf4j*jar, its better we copy log4j-slf4j-2.17.1.jar into same directory. </li>
<li> Change the file ownership and permission to what original log4j file have. </li>
<li> Scan for any web.xml, MANIFEST.MF under same folder structure (can be same folder/or one level above). Confirm no hardcoded class path point to deleted library, else we will have to update the reference.</li>
<li> Go to product binary folder, for example bin/bin64. Check for the script used to initialize the application, e.g. startup.sh, confirm no hardcoded classpath point to deleted library, else we will have to update the reference. </li>
</ol>
As mentioned clearly by https://logging.apache.org/log4j/2.x/manual/migration.html
The Log4j 1.x bridge is useful when:
<ol>
<li> the application itself is (maybe partly) still using the Log4j 1.x API, or if </li>
<li> the application depends on a library which depends on the Log 1.x API, or </li>
<li> the application needs to support logging configurations in the old Log4j 1.x format. </li>
</ol>
This is why I started my own kind of wrapper. 
<b>bridge-ext</b>
<ol>
<li> contain source code to further bridge between log4j-1.x to 2.x </li>
<li> Added Class and implementation for org.apache.log4j.Hierarchy, org.apache.log4j.RollingFileAppender, org.apache.log4j.FileAppender, org.apache.log4j.jmx.HierarchyDynamicMBean, org.apache.log4j.helpers.LogLog </li>
<li> Overwrite org.apache.log4j.Logger, org.apache.log4j.xml.DOMConfigurator </li>
</ol>
<b>bridge-ext-rebuild</b>
<ol>
  <li> rebuild version of bridge-ext + log4j-1.2-api-2.17.1.jar </li>
 </ol>
 <b>apache.log4j2</b>
<ol>
  <li> a.	One jar containing all class makeup for bridge-ext-rebuild.jar, log4j-api-2.17.1.jar, log4j-core-2.17.1.jar, log4j-jcl-2.17.1.jar, log4j-slf4j-2.17.1.jar, log4j-web-2.17.1.jar </li>
 </ol>
