

package streetdirectory.mobile.service;

import streetdirectory.mobile.core.service.HttpImageConnectionInput;

public abstract class SDHttpImageServiceInput extends HttpImageConnectionInput
{

    public SDHttpImageServiceInput()
    {
    }

    public SDHttpImageServiceInput(int i, int j)
    {
        super(i, j);
    }

    public SDHttpImageServiceInput(int i, int j, int k)
    {
        super(i, j, k);
    }

    public SDHttpImageServiceInput(int i, int j, int k, int l)
    {
        super(i, j, k, l);
    }
}
