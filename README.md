```
      _
  ___| |__   ___  ___ ___
 / __| '_ \ / _ \/ __/ __|
| (__| | | |  __/\__ \__ \
 \___|_| |_|\___||___/___/
```

✝️ This software was written in the name of the __Father__ and of the __Son__ and of the __Holy Spirit__; Amen.

# About

Experience the millenium-old game of Chess in an objectively inferior way! Very limited in scope -
this was one of my first personal projects: recreating Chess using Java's Swing GUI widget toolkit.
No online multiplayer here; just poor hitboxes and event output sent to stdout rather than a real
text pane.

<div>
    <img
        src="https://www.neilkingdom.xyz/static/images/git/chess/start_menu_1.png"
        width="49%"
        alt="Splash Screen (Green)"
    />
    <img
        src="https://www.neilkingdom.xyz/static/images/git/chess/start_menu_2.png"
        width="49%"
        alt="Splash Screen (Drab)"
    />
</div>
<img
    src="https://www.neilkingdom.xyz/static/images/git/chess/gameplay.png"
    alt="Gameplay"
/>

# Running the application

To run the application, simply execute the shell script provided

```console
./run.sh
```

I can't be bothered to port the script to Windows, but it should be pretty straightforward since most of the
Java command line tools are cross platform. The script will build the jar file and place it in the build
directory. It will also compile the javadoc comments located in ./src/assets/javadoc.
