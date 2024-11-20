package jospi.endpoints;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import jospi.client.core.OsuApiClient;
import jospi.client.resources.ClientUtil;
import jospi.enums.misc.Ruleset;
import jospi.enums.rankings.RankingFilter;
import jospi.enums.rankings.UserRankingType;
import jospi.iterator.AsyncLazyEnumerable;
import jospi.iterator.ExitToken;
import jospi.models.rankings.RankingsBundle;
import jospi.models.rankings.Spotlight;
import jospi.models.rankings.SpotlightRankings;
import jospi.models.users.User;
import jospi.models.users.UserStatistics;

public final class Rankings {
	private static final String BASE = "/rankings/";

	private OsuApiClient client;

	protected Rankings(OsuApiClient client) {
		this.client = client;
	}
	
	public CompletableFuture<User[]> getKudosuRanking(int page) {
		return CompletableFuture.supplyAsync(() -> 
			client.getJson("/rankings/kudosu?page"+page)
		);
	}

	public CompletableFuture<Spotlight[]> getSpotlights() {
		return CompletableFuture.supplyAsync(() -> 
			client.getJson("/spotlights")
		);
	}

	public AsyncLazyEnumerable<String, UserStatistics[]> getPerformanceRanking(Ruleset ruleset, String countryCode, String variant) {
		return getPerformanceRanking(ruleset, countryCode, RankingFilter.ALL, variant);
	}

	public AsyncLazyEnumerable<String, SpotlightRankings> getSpotlightRanking(Ruleset ruleset, String spotlightId) {
		return getSpotlightRanking(ruleset, RankingFilter.ALL, spotlightId);
	}

	public AsyncLazyEnumerable<String, RankingsBundle> getRanking(Ruleset ruleset, UserRankingType type) {
		return getRanking(ruleset, type, RankingFilter.ALL);
	}

	// REQUIRES USER

	public AsyncLazyEnumerable<String, UserStatistics[]> getPerformanceRanking(Ruleset ruleset, String countryCode, RankingFilter filter, String variant) {
		if (filter.equals(RankingFilter.FRIENDS)) client.requiresUser();
		AsyncLazyEnumerable<String, RankingsBundle> enumerableBundle = getRankingInternal(ruleset, UserRankingType.PERFORMANCE, countryCode, filter, null, variant);
		Function<CompletableFuture<RankingsBundle>, CompletableFuture<UserStatistics[]>> func = (CompletableFuture<RankingsBundle> bundle) -> {
			return CompletableFuture.supplyAsync(() -> ClientUtil.awaitTask(bundle).getRankings());
		};
		return enumerableBundle.append(func);
	}

	public AsyncLazyEnumerable<String, SpotlightRankings> getSpotlightRanking(Ruleset ruleset, RankingFilter filter, String spotlightId) {
		if (filter.equals(RankingFilter.FRIENDS)) client.requiresUser();
		AsyncLazyEnumerable<String, RankingsBundle> enumerableBundle = getRankingInternal(ruleset, UserRankingType.PERFORMANCE, null, filter, spotlightId, null);
		Function<CompletableFuture<RankingsBundle>, CompletableFuture<SpotlightRankings>> func = (CompletableFuture<RankingsBundle> bundle) -> {
			return CompletableFuture.supplyAsync(() -> (new SpotlightRankings(ClientUtil.awaitTask(bundle))));
		};
		return enumerableBundle.append(func);
	}

	public AsyncLazyEnumerable<String, RankingsBundle> getRanking(Ruleset ruleset, UserRankingType type, RankingFilter filter) {
		if (filter.equals(RankingFilter.FRIENDS)) client.requiresUser();
		return getRankingInternal(ruleset, type, null, filter, null, null);
	}

	//Internal method
	private AsyncLazyEnumerable<String, RankingsBundle> getRankingInternal(Ruleset ruleset, UserRankingType type, String countryCode, RankingFilter filter, String spotlightId, String variant) {
		ExitToken<String> token = new ExitToken<>("", Objects::nonNull);
		Function<ExitToken<String>, CompletableFuture<RankingsBundle>> func = t -> 
			CompletableFuture.supplyAsync(() -> {
				Map<String, Object> params = new HashMap<>();
				params.put("mode", ruleset);
				params.put("type", type);
				params.put("country", countryCode);
				params.put("cursor", token.getToken());
				params.put("filter", filter);
				params.put("spotlight", spotlightId);
				params.put("variant", variant);
				RankingsBundle bundle = client.getJson(BASE + "packs", params);
				token.setNext(bundle.getCursorString());
				return bundle;
			});
		return new AsyncLazyEnumerable<>(func, token);
	}
}