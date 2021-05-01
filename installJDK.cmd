@echo off
echo %date% %time% installJDK.cmd version 1.0.0
set CHOICE=%1

if "%CHOICE%" equ "AdoptOpenJDK11" (
    set INSTALL_TARGET_ROOT=c:\program files\AdoptOpenJDK
	echo INSTALL_TARGET_ROOT=%INSTALL_TARGET_ROOT%

    if not exist "%INSTALL_TARGET_ROOT%" (
        echo %date% %time%  installing AdoptOpenJDK11 ...
        choco install -y --force AdoptOpenJDK11
    ) else (
        echo %date% %time% JDK Already installed - choco install not required
    )

    if not exist "%INSTALL_TARGET_ROOT%\jdk" (
		echo %date% %time% copying JDK to a known standard path ...
		dir  /b "%INSTALL_TARGET_ROOT%\jdk-11*" > %TEMP%\tmp
		set /p JDK=<%TEMP%\tmp
		echo installed JDK=%JDK%
		xcopy /s "%INSTALL_TARGET_ROOT%\%JDK%" "%INSTALL_TARGET_ROOT%\jdk\"
	) else (
        echo %date% %time% JDK Already copied
	)
	echo %date% %time% JDK now at %INSTALL_TARGET_ROOT%\jdk
) else (
    echo %date% %time% %JDK% is not known for the moment
)

