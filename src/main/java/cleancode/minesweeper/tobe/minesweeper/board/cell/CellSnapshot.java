package cleancode.minesweeper.tobe.minesweeper.board.cell;

import java.util.Objects;

public class CellSnapshot {
    private final CellSnapshotStatus status;
    private final int nearbyLandMIneCount;

    private CellSnapshot(CellSnapshotStatus status, int nearbyLandMIneCount) {
        this.status = status;
        this.nearbyLandMIneCount = nearbyLandMIneCount;
    }

    public static CellSnapshot of(CellSnapshotStatus status, int nearbyLandMIneCount) {
        return new CellSnapshot(status, nearbyLandMIneCount);
    }

    public static CellSnapshot ofEmpty() {
        return of(CellSnapshotStatus.EMPTY, 0);
    }

    public static CellSnapshot ofFlag() {
        return of(CellSnapshotStatus.FLAG, 0);
    }

    public static CellSnapshot ofLandMine() {
        return of(CellSnapshotStatus.LAND_MINE, 0);
    }

    public static CellSnapshot ofNumber(int nearbyLandMIneCount) {
        return of(CellSnapshotStatus.NUMBER, nearbyLandMIneCount);
    }

    public static CellSnapshot ofUnchecked() {
        return of(CellSnapshotStatus.UNCHECKED, 0);
    }

    public CellSnapshotStatus getStatus() {
        return status;
    }

    public boolean isSameStatus(CellSnapshotStatus cellSnapshotStatus) {
        return this.status == cellSnapshotStatus;
    }

    public int getNearbyLandMIneCount() {
        return nearbyLandMIneCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellSnapshot snapshot = (CellSnapshot) o;
        return getNearbyLandMIneCount() == snapshot.getNearbyLandMIneCount() && getStatus() == snapshot.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getNearbyLandMIneCount());
    }
}
