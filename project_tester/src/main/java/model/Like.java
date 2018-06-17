package model;

public class Like implements LikeIF {

	private long userId;
	private long itemId;
	private LikeItemTyp typ;

	public Like(LikeItemTyp typ, long userId, long itemId) {
		this.typ = typ;
		this.userId = userId;
		this.itemId = itemId;
	}

	@Override
	public long getItemId() {
		return itemId;
	}

	@Override
	public long getUserId() {
		return userId;
	}
	
	@Override
	public LikeItemTyp getItemTyp() {
		return typ;
	}
}