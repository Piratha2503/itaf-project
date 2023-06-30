package com.ii.testautomation.dto.response.bulkResponse;

import com.ii.testautomation.response.common.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class MainModulesBResponse
{
    private List<Integer> Null_Value_RowNumbers;
    private List<Integer> Name_Already_Exist_RowNumbers;
    private List<Integer> Prefix_Already_Exist_RowNumbers;
    private List<Integer> ModulesId_NotFound_RowNumbers;
    private String Constants;
    private BaseResponse Response;

    public MainModulesBResponse(BaseResponse baseResponse,String Constants, List<Integer> Null_Value_RowNumbers, List<Integer> Name_Already_Exist_RowNumbers,
                                List<Integer> Prefix_Already_Exist_RowNumbers, List<Integer> ModulesId_NotFound_RowNumbers)
    {
        this.Response = baseResponse;
        this.Constants = Constants;
        this.Null_Value_RowNumbers = Null_Value_RowNumbers;
        this.Name_Already_Exist_RowNumbers = Name_Already_Exist_RowNumbers;
        this.Prefix_Already_Exist_RowNumbers = Prefix_Already_Exist_RowNumbers;
        this.ModulesId_NotFound_RowNumbers = ModulesId_NotFound_RowNumbers;

    }
}
