reverse:
	adb reverse tcp:8081 tcp:8081 && adb reverse tcp:3449 tcp:3449 

prod-build:
	lein prod-build

dist: prod-build
	cd android && ./gradew assembleRelease
