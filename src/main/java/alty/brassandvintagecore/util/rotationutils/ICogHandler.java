package alty.brassandvintagecore.util.rotationutils;

import javax.annotation.Nonnull;

public interface ICogHandler {
	/**
	 * Should redirect to the AxleHandler propogate method.
	 */
	public void connect(@Nonnull IAxisHandler masterIn, byte key, double rotationRatioIn, double lastRadius);

	public IAxleHandler getAxle();
}
