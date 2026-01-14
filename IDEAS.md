parallel and sequential composition of events
each event is passed a clock time, and a context dictionary (for current instrument, dynamics, scale, ...)
- context may contain functions from time to value (envelopes, etc.)
- use Scala "given" magic to make context-/time-dependent values look like simple variables?
transformations (scaling & shifting)?
easing? As a transform?
multiple nested clocks?
- passed in context? How to update through a transform?
- global clock and context, then each nested one has an associated transform
- lookup named parameters from contexts (inner to outer scope), and resolve using time from corresponding clock
- rendering converts everything to the global clock
also have an optional position (x,y), perhaps based on time?
- this would be for animations (could also do stereo position)
- have origin, min, and max for each dimension, and combining rules (overlap, beside, ...) in each dimension
- compute the transform based on the time


Music examples:
- common music entry
    - easy to describe a sequence of pitches and durations, plus chords and multiple voices
- different scales, tunings, and instruments
    - get current note names & their pitches from the context? through syntax imports?
    - MIDI and/or OSC
    - also support percussion, and microtones (note as number rather than name)
- triplets, etc.
    - stretch one event to match the duration of another
- ornaments and grace notes
    - treat a group of events as a single one, with reference point in the middle
- transposition
    - map a function over all subevents, or change a tonic parameter in the context?
- dynamics
    - volume depends on time across a compound event
- accelerando
    - local clock eases to a different rate
- phasing
    - application of the above
- operations on sequences
    - canons, 12-tone, change ringing, interpolation, ...


Build on/recycle from https://github.com/DePauwREU2013/scales? Doodle/Compose? ScalaCollider? Cats Effect Scheduler?