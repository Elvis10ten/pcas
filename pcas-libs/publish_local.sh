rm -r ~/.m2/repository/com/fluentbuild/pcas-libs/
rm -r ~/.m2/repository/com/fluentbuild/pcas-libs-android/
rm -r ~/.m2/repository/com/fluentbuild/pcas-libs-android-debug/
rm -r ~/.m2/repository/com/fluentbuild/pcas-libs-jvm/
rm -r ~/.m2/repository/com/fluentbuild/pcas-libs-metadata/
./gradlew clean
./gradlew build publishToMavenLocal