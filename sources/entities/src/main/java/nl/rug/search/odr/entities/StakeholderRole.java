
package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class StakeholderRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stakeholderRoleId;

    private String name;

    private boolean common;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);

        this.name = name;
    }

    private void checkName(String name) {
        if (name == null) {
            throw new NullPointerException("A valid name is required.");
        } else if (name.trim().isEmpty()) {
            throw new RuntimeException("A valid name is required.");
        }
    }

    public Long getStakeholderRoleId() {
        return stakeholderRoleId;
    }

    public void setStakeholderRoleId(Long stakeholderRoleId) {
        this.stakeholderRoleId = stakeholderRoleId;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StakeholderRole other = (StakeholderRole) obj;
        if (this.stakeholderRoleId != other.stakeholderRoleId && (this.stakeholderRoleId == null ||
                !this.stakeholderRoleId.equals(other.stakeholderRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.stakeholderRoleId != null ? this.stakeholderRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "StakeholderRole{" + "stakeholderRoleId=" + stakeholderRoleId + '}';
    }


}
