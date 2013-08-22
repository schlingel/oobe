@echo off
set START_DIR=%~dp0
set ARTICLESDIR=%1
set TARGET_DIR=%2

cd %ARTICLESDIR%

IF [%3]==[] goto multiple_files

:single_file
@htlatex %3
goto copy_websites

:multiple_files
echo Multiple files!
for /f %%f in ('dir /b *.tex') do @htlatex %%f

:copy_websites
echo ...built web site
echo starting coping process to %TARGET_DIR%

for /f %%f in ('dir /b *.png') do xcopy %%f %TARGET_DIR%\img /C /Y
for /f %%f in ('dir /b *.css') do xcopy %%f %TARGET_DIR%\css /C /Y
for /f %%f in ('dir /b *.html') do xcopy %%f %TARGET_DIR%\ /C /Y
echo ...moved web site

del *.png
del *.html
del *.css
del *.4ct
del *.4tc
del *.aux
del *.dvi
del *.idv
del *.lg
del *.log
del *.tmp
del *.xref

echo ...removed meta files

echo ...finished building html files
cd %START_DIR%