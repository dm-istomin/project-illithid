.PHONY: reverse prod-build bundle dist default

default: dist

reverse:
	adb reverse tcp:8081 tcp:8081 && adb reverse tcp:3449 tcp:3449 

bundle:
	react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output android/app/src/main/assets/index.android.bundle --sourcemap-output android/app/src/main/assets/index.android.map --assets-dest android/app/src/main/res/

prod-build:
	lein prod-build

android/app/build/outputs/apk/app-debug.apk: prod-build bundle
	cd android && ./gradlew assembleRelease

dist/illithid.apk: android/app/build/outputs/apk/app-debug.apk
	mkdir -p dist
	cp $< $@

dist: dist/illithid.apk
