package osuapi.endpoints;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import osuapi.client.core.OsuApiClient;
import osuapi.iterator.AsyncLazyEnumerable;
import osuapi.iterator.ExitToken;
import osuapi.enums.matches.MatchBundleSort;
import osuapi.models.matches.Match;
import osuapi.models.matches.MatchBundle;
import osuapi.models.matches.MatchesBundle;
import osuapi.models.structs.MatchEventParams;

public class Matches {
    private static final String BASE = "/matches/";

    private OsuApiClient client;

	protected Matches(OsuApiClient client) {
		this.client = client;
	}

	public AsyncLazyEnumerable<String, Match[]> getMatches(int limit, MatchBundleSort sort) {
		ExitToken<String> token = new ExitToken<>("", Objects::nonNull);
		Function<ExitToken<String>, CompletableFuture<Match[]>> func = t -> 
			CompletableFuture.supplyAsync(() -> {
				Map<String, Object> params = new HashMap<>();
				params.put("limit", limit);
				params.put("sort", sort);
				MatchesBundle packs = client.getJson(BASE, params);
				token.setNext(packs.getCursorString());
				return packs.getMatches();
			});
		return new AsyncLazyEnumerable<>(func, token);
	}

	public CompletableFuture<MatchBundle> getMatch(int matchId, MatchEventParams params) {
		params.constructor();
		return client.getJsonAsync(BASE+matchId+params.process());
	}
}
