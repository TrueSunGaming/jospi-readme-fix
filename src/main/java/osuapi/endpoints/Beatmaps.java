package osuapi.endpoints;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import osuapi.client.OsuApiClient;
import osuapi.enums.Ruleset;
import osuapi.models.beatmaps.Beatmap;
import osuapi.models.beatmaps.BeatmapExtended;
import osuapi.models.beatmaps.DifficultyAttributes;
import osuapi.models.custom.OsuApiException;
import osuapi.models.scores.Score;
import osuapi.models.scores.UserBeatmapScore;

@Component
public class Beatmaps {
	
	@Autowired
	private OsuApiClient client;
	
	public CompletableFuture<Beatmap> lookupBeatmapChecksum(String checksum) {
		try {
			return lookupBeatmapInternal("checksum=" + URLEncoder.encode(checksum, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(new Beatmap());
	}
	
	public CompletableFuture<Beatmap> lookupBeatmapFilename(String filename) {
		return lookupBeatmapInternal("filename=" + filename);
	}
	
	public CompletableFuture<Beatmap> lookupBeatmapId(String id) {
		return lookupBeatmapInternal("id=" + id);
	}
	
	private CompletableFuture<Beatmap> lookupBeatmapInternal(String query) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/lookup?" + query, new Beatmap());
			} catch (OsuApiException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	public CompletableFuture<UserBeatmapScore> getUserBeatmapScore(int beatmapId, int userId, Ruleset ruleset, String mods) {
		Map<String, Object> params = new HashMap<>();
		params.put("mode", ruleset);
		params.put("mods", mods);
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/"+beatmapId+"/scores/users/"
						+userId+client.buildQueryString(params), new UserBeatmapScore());
			} catch (OsuApiException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	public CompletableFuture<Score[]> getUserBeatmapScores(int beatmapId, int userId, Ruleset ruleset) {
		Map<String, Object> params = new HashMap<>();
		params.put("mode", ruleset);
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/"+beatmapId+"/scores/users/"
						+userId+"/all"+client.buildQueryString(params), new Score[Integer.MAX_VALUE]);
			} catch (OsuApiException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	public List<CompletableFuture<BeatmapExtended>> getBeatmaps(int[] ids) {
		return Arrays.stream(ids.length>50? Arrays.copyOf(ids, 50) : ids).boxed().map(this::getBeatmap).collect(Collectors.toList());
	}
	
	public CompletableFuture<BeatmapExtended> getBeatmap(int id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/"+id, new BeatmapExtended());
			} catch (OsuApiException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	public CompletableFuture<DifficultyAttributes> getDifficultyAttributes(int id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/"+id+"/attributes", new DifficultyAttributes(), HttpMethod.POST);
			} catch (OsuApiException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
}
