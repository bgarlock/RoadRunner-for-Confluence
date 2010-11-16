# Mac OS X java apps have an ugly application menu title, but we can set it 
# with a VM argument. 
APP_NAME="RoadRunner"
APPLE_ARGS="-Xdock:name=$APP_NAME"
java -Xms256m -Xmx256m $APPLE_ARGS -jar rr.jar

