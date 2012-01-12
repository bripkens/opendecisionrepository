REM @echo off
REM short skript to copy plugin to VP and restart VP
set version=VP Suite 5.3
set pluginVersion=ODR-VP-Plugin-0.2.4-SNAPSHOT
set backup=0


REM kill Visual Paradigm
taskkill /F /IM "run_vpuml.exe" /T

REM create directories
mkdir "C:\Program Files\%version%\plugins\ODR\lib\"
mkdir "C:\Program Files\%version%\plugins\ODR\images\"

REM create backup of existing customization
if exist "C:\Program Files\%version%\resources\vpuicustomization.xml" GOTO RENAMETOBACKUP
GOTO COPY
:RENAMETOBACKUP
set backup=1
rename "C:\Program Files\%version%\resources\vpuicustomization.xml" vpuicustomization.backup

REM copy ODR customization
:COPY
copy "%CD%\vpuicustomization.xml" "C:\Program Files\%version%\resources\vpuicustomization.xml"
copy "%CD%\usersettings.xml" "C:\Program Files\%version%\plugins\ODR\usersettings.xml"
copy "%CD%\images\*.*" "C:\Program Files\%version%\plugins\ODR\images\*.*"


REM copy Plug-in to Visual Paradigm
copy "%CD%\target\%pluginVersion%.jar" "C:\Program Files\%version%\plugins\ODR\lib\%pluginVersion%.jar"
copy "%CD%\plugin.xml" "C:\Program Files\%version%\plugins\ODR\plugin.xml"


REM start Visual Paradigm with new Plug-in
"C:\Program Files\%version%\launcher\run_vpuml.exe"
GOTO CONTINUEWHENCLOSED


:CONTINUEWHENCLOSED
REM delete customization file and restore old one
del "C:\Program Files\%version%\resources\vpuicustomization.xml"
REM if backup==0 GOTO END
rename "C:\Program Files\%version%\resources\vpuicustomization.backup" vpuicustomization.xml

RMDIR "C:\Users\%Username%\vpworkspace" /s /q

:END