package com.fluentbuild.pcas.peripheral

data class PeripheralBond(
    val profile: PeripheralProfile,
    val hotState: State,
    val steadyState: State
) {

    override fun equals(other: Any?): Boolean {
        if(other !is PeripheralBond) return false
        return profile == other.profile && steadyState == other.steadyState
    }

    override fun hashCode(): Int {
        var result = profile.hashCode()
        result = 31 * result + steadyState.hashCode()
        return result
    }

    enum class State {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING;

        fun getSteadyState(): State {
            return when(this) {
                CONNECTED -> CONNECTED
                CONNECTING -> DISCONNECTED
                DISCONNECTED -> DISCONNECTED
                DISCONNECTING -> CONNECTED
            }
        }
    }
}