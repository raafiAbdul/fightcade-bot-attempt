import express, { Request, Response } from 'express';
import { GetUserReplays, GetGame, GetReplayURL } from 'fightcade-api';

const app = express();
const PORT = 8080;

app.use(express.json());

app.get('/api/last-match', async (req: Request, res: Response) => {
    const username = req.query.username as string;
    const targetGameId = ((req.query.gameid as string) || 'jojoba').toLowerCase();

    if (!username) {
        return res.status(400).json({ error: 'Parameter "username" is required.' });
    }

    try {
        // 1. Verify target game exists
        const game = await GetGame(targetGameId);
        if (!game) {
            return res.status(404).json({ error: `Game ID '${targetGameId}' was not found on Fightcade.` });
        }

        // 2. Query replays using 1 argument to avoid TS overload conflicts
        const allReplays = await GetUserReplays(username);

        if (!allReplays || allReplays.length === 0) {
            return res.status(404).json({
                error: `No recent matches found for user '${username}'.`,
            });
        }

        // 3. Filter for ranked jojoba matches
        const jojobaReplays = allReplays.filter(
            (r) => r.ranked && r.gameid?.toLowerCase() === targetGameId
        );

        if (jojobaReplays.length === 0) {
            return res.status(404).json({
                error: `No recent ranked match found for game '${targetGameId}' under user '${username}'.`,
            });
        }

        const lastReplay = jojobaReplays[0];

        const playerObj = lastReplay.players?.find(
            (p) => p.name.toLowerCase() === username.toLowerCase()
        );
        const opponentObj = lastReplay.players?.find(
            (p) => p.name.toLowerCase() !== username.toLowerCase()
        );

        return res.json({
            gameid: lastReplay.gameid,
            gameTitle: game.name || "JoJo's Bizarre Adventure",
            playerUsername: playerObj ? playerObj.name : username,
            playerScore: playerObj ? playerObj.score : 0,
            opponentUsername: opponentObj ? opponentObj.name : 'Unknown',
            opponentScore: opponentObj ? opponentObj.score : 0,
            replayUrl: GetReplayURL(lastReplay),
        });
    } catch (err: any) {
        console.error('Error in Fightcade API server bridge:', err);
        return res.status(500).json({ error: err.message || 'Internal server error' });
    }
});

app.listen(PORT, () => {
    console.log(`[TypeScript Server] Bridge running at http://localhost:${PORT}`);
});