rm -r ~/.m2/repository/com/fluentbuild/pcas/
rm -r ~/.m2/repository/com/fluentbuild/pcas-android/
rm -r ~/.m2/repository/com/fluentbuild/pcas-android-debug/
rm -r ~/.m2/repository/com/fluentbuild/pcas-jvm/
rm -r ~/.m2/repository/com/fluentbuild/pcas-metadata/
./gradlew clean
./gradlew build publishToMavenLocal