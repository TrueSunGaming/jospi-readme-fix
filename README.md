
# Jospi
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.wyvrtn/jospi)

A simple, in-development async wrapper for the osu! API v2

*Note: As of current, only Java version 1.8 is supported*

# Maven

Note that this project is still in development, so it will be expected that some of the features may not work as intended.

To import using Maven:
```
<dependency>
    <groupId>io.github.wyvrtn</groupId>
    <artifactId>jospi</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Features

Currently, I'm ahead of minisbett in feature implementation
```
Beatmap Packs (100% 2/2)
  [✓] /beatmaps/packs
  [✓] /beatmaps/packs/{tag}

Beatmaps (100% 7/7)
  [✓] /beatmaps
  [✓] /beatmaps/lookup
  [✓] /beatmaps/{beatmap}
  [✓] /beatmaps/{beatmap}/attributes
  [✓] /beatmaps/{beatmap}/scores
  [✓] /beatmaps/{beatmap}/scores/users/{user}
  [✓] /beatmaps/{beatmap}/scores/users/{user}/all

Beatmap Sets (100% 2/2)
  [✓] /beatmapsets/lookup
  [✓] /beatmapsets/{beatmapset}

Changelogs (100% 3/3)
  [✓] /changelog
  [✓] /changelog/{buildOrStream}
  [✓] /changelog/{stream}/{build}

Comments (100% 2/2)
  [✓] /comments
  [✓] /comments/{comment}

Events (100% 1/1)
  [✓] /events

Home (100% 1/1)
  [✓] /search

Matches (100% 2/2)
  [✓] /matches
  [✓] /matches/{match}

Multiplayer (100% 2/2)
  [✓] /rooms/{room}/playlist/{playlist}/scores
  [✓] /rooms

News (100% 2/2)
  [✓] /news
  [✓] /news/{news}

Ranking (100% 3/3)
  [✓] /rankings/kudosu
  [✓] /rankings/{mode}/{type}
  [✓] /spotlights

Users (100% 6/6)
  [✓] /users
  [✓] /users/{user}/{mode?}
  [✓] /users/{user}/kudosu
  [✓] /users/{user}/scores/{type}
  [✓] /users/{user}/beatmapsets/{type}
  [✓] /users/{user}/recent_activity

Wiki (100% 1/1)
  [✓] /wiki/{locale}/{path}

Undocumented (0% 0/?)
  To be added
```
