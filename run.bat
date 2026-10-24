@echo off
chcp 65001 > nul
cd /d "%~dp0"
echo Запуск приложения...
java -jar target\course-project-1.0.0.jar
pause