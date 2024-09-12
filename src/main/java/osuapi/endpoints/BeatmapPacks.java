package osuapi.endpoints;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import osuapi.client.OsuApiClient;
import osuapi.enums.BeatmapPackType;
import osuapi.models.beatmaps.BeatmapPack;
import osuapi.models.beatmaps.BeatmapPackExtended;
import osuapi.models.custom.AsyncLazyEnumerator;
import osuapi.models.custom.ExitToken;
import osuapi.models.custom.OsuApiException;

// API docs: https://osu.ppy.sh/docs/index.html#beatmap-packs
public class BeatmapPacks {
	
	@Autowired
	private OsuApiClient client;
	
	public AsyncLazyEnumerator<String, BeatmapPack[]> getBeatmapPacks(final BeatmapPackType type) {
		String url = "/beatmaps/packs";
		ExitToken<String> token = new ExitToken<>("", Objects::nonNull);
		Function<ExitToken<String>, CompletableFuture<BeatmapPack[]>> func = t -> 
			CompletableFuture.supplyAsync(() -> {
				BeatmapPackExtended packs = null;
				Map<String, Object> params = new HashMap<>();
				params.put("type", type==null? "Standard" : type.toString());
				params.put("cursor_string", token.getToken());
				try {
					packs = client.getJson(url 
							+ client.buildQueryString(params), new BeatmapPackExtended());
				} catch (UnsupportedEncodingException | OsuApiException e) {
					e.printStackTrace();
				}
				token.setNext(packs.getCursorString());
				return packs.getBeatmapPacks();
			});
		return new AsyncLazyEnumerator<>(func, token);
	}
	
	public CompletableFuture<BeatmapPack> getBeatmapPack(String tag) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return client.getJson("/beatmaps/packs/"+tag, new BeatmapPack());
			} catch (OsuApiException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
}
